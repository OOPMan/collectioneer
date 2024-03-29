package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.cli.{Config, Subconfig, Subject, Verb}
import com.oopman.collectioneer.db.entity.projected.{Collection, Property, PropertyValue}
import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.h2.H2DatabaseBackend
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.{ParentCollection, SourceOfPropertiesAndPropertyValues}
import com.oopman.collectioneer.db.{Injection, dao, entity, traits}
import com.oopman.collectioneer.plugins.CLIPlugin
import com.oopman.collectioneer.plugins.gatcg.given
import com.oopman.collectioneer.plugins.gatcg.properties.*
import com.typesafe.scalalogging.LazyLogging
import io.circe.*
import io.circe.generic.auto.*
import io.circe.optics.JsonPath.*
import io.circe.parser.*
import io.circe.syntax.*
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}
import sttp.client3.*
import sttp.client3.circe.*

import java.io.File
import java.util.UUID
import scala.language.postfixOps
import scala.util.*

case class DownloadDatasetResult
(
  datasetPath: Option[String] = None,
  datasetSize: Int = 0,
  downloadSucceeded: Boolean = true,
  errorMessage: Option[String] = None
)

class GATCGCLIPlugin extends CLIPlugin with LazyLogging:
  val defaultDatasetPath = os.pwd / "gatcg.json"

  def getName: String = "Grand Archive TCG"

  def getShortName: String = "GATCG"

  def getVersion: String = "master"

  def getMigrationLocations(databaseBackend: DatabaseBackend): Seq[String] = databaseBackend match
    case _: H2DatabaseBackend => Seq("classpath:com/oopman/collectioneer/plugins/gatcg/migrations/h2")

  def getDefaultSubconfig: GATCGPluginConfig = GATCGPluginConfig()

  def getSubconfigFromConfig(config: Config): GATCGPluginConfig =
    config.subconfigs.getOrElse(getShortName, getDefaultSubconfig).asInstanceOf[GATCGPluginConfig]

  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Config => Json, List[OParser[_, Config]])] =
    val datasetPathOpt = builder.opt[File]("gatcg-json-dataset-path")
      .optional()
      .action((f, config) => config.copy(subconfigs = config.subconfigs.updated(getShortName, getSubconfigFromConfig(config).copy(grandArchiveTCGJSON = Some(f)))))
      .text("Something")
    List(
      (Verb.imprt, Subject.dataset, importDataset, List(datasetPathOpt)),
      (Verb.download, Subject.dataset, downloadDataset, List(datasetPathOpt))
    )

  def getData
  (
    client: SimpleHttpClient = SimpleHttpClient(),
    baseUri: String = "https://api.gatcg.com",
    page: Int = 1,
    pageSize: Int = 50,
    delayBetweenRequests: Long = 500
  ): Try[Vector[Json]] =
    logger.info(s"Retrieving page $page with page size $pageSize from $baseUri")
    val request = basicRequest
      .get(uri"$baseUri/cards/search?page=$page&page_size=$pageSize")
      .response(asJson[io.circe.Json])
    val response = client.send(request)
    response.body match
      case Left(body) =>
        val message = s"Error downloading $page with page size $pageSize from $baseUri: ${response.code}"
        logger.error(message)
        Failure(RuntimeException(message))
      case Right(body) =>
        val hasMore = root.has_more.boolean.getOption(body).getOrElse(false)
        val data = root.data.arr.getOption(body).getOrElse(Vector())
        if !hasMore then
          Success(data)
        else
          this.synchronized { wait(delayBetweenRequests) }
          getData(client, baseUri, page + 1, pageSize).map(newData => data :++ newData)

  def importDataset(config: Config) =
    val pathOption = getSubconfigFromConfig(config).grandArchiveTCGJSON.map(f => os.FilePath(f))
    val dataOption: Option[Vector[Json]] = pathOption
      .map {
        case p: os.Path     => parse(os.read(p))
        case p: os.RelPath  => parse(os.read(os.pwd / p))
        case p: os.SubPath  => parse(os.read(os.pwd / p))
      }
      .map {
        case Left(parsingException) =>
          logger.error("Failed to parse GATCG JSON Dataset", parsingException)
          Vector()
        case Right(json) =>
          json.asArray.getOrElse(Vector())
      }
    val dataTry: Try[Json] = dataOption match {
      case Some(Vector()) =>
        val message = "GATCG JSON Dataset contains no data or the root element is not an Array"
        logger.error(message)
        Failure(RuntimeException(message))
      case Some(value) =>
        logger.info(s"Loaded ${value.length} items from GATCG JSON Dataset")
        Success(value.asJson)
      case None =>
        logger.warn("No GATCG JSON Dataset passed so data will be retrieved from the GATCG Index API")
        getData().map(_.asJson)
    }
    val modelsTry = dataTry.flatMap(data => {
      import Models.*
      data.as[List[Card]].toTry
    })
    modelsTry.map(cards => {
      // TODO: Query for existing sets, map set name to UUID of Collection representing that Set
      val existingSetsMap: Map[(String, String), UUID] = Map()
      // TODO: Query for existing cards, map card id to UUID of Collection representing that Card
      val existingCardsMap: Map[String, UUID] = Map()
      // TODO: Query for existing editions, map edition id to UUID of Collection reprsenting that Edition
      val existingEditions: Map[String, UUID] = Map()
      // Generate Sets
      val sets = cards.flatMap(_.editions.map(_.set)).distinctBy(set => (set.prefix, set.language))
      val setMap = Map.from(sets.map(set => ((set.prefix, set.language), Collection(
        pk = existingSetsMap.getOrElse((set.prefix, set.language), UUID.randomUUID),
        virtual = true,
        propertyValues = List(
          PropertyValue(property = SetProperties.setName, varcharValues = List(set.name)),
          PropertyValue(property = SetProperties.setPrefix, varcharValues = List(set.prefix)),
          PropertyValue(property = SetProperties.setLanguage, varcharValues = List(set.language)),
          PropertyValue(property = CommonProperties.isGATCGSet, booleanValues = List(true))
        )
      ))))
      // Generate Collections containing CardProperties
      val cardsMap = Map.from(cards.map(card => (card.uuid, Collection(
        pk = existingCardsMap.getOrElse(card.uuid, UUID.randomUUID),
        virtual = true,
        propertyValues = List(
          PropertyValue(property = CardProperties.uuid, varcharValues = List(card.uuid)),
          PropertyValue(property = CardProperties.element, varcharValues = List(card.element)),
          PropertyValue(property = CardProperties.types, varcharValues = card.types),
          PropertyValue(property = CardProperties.classes, varcharValues = card.classes),
          PropertyValue(property = CardProperties.subTypes, varcharValues = card.subtypes),
          PropertyValue(property = CardProperties.effect, varcharValues = card.effect_raw.map(List(_)).getOrElse(Nil)),
          PropertyValue(property = CardProperties.memoryCost, tinyintValues = card.cost_memory.map(i =>List(i.toByte)).getOrElse(Nil)),
          PropertyValue(property = CardProperties.reserveCost, tinyintValues = card.cost_reserve.map(i => List(i.toByte)).getOrElse(Nil)),
          PropertyValue(property = CardProperties.level, tinyintValues = card.level.map(i => List(i.toByte)).getOrElse(Nil)),
          PropertyValue(property = CardProperties.speed, varcharValues = card.speed.map(b => List(if b then "Fast" else "Slow")).getOrElse(Nil)),
          PropertyValue(property = CardProperties.legality, jsonValues = card.legality.map(j => List(j.spaces2.getBytes)).getOrElse(Nil)),
          PropertyValue(property = CardProperties.power, tinyintValues = card.power.map(p => List(p.toByte)).getOrElse(Nil)),
          PropertyValue(property = CardProperties.life, tinyintValues = card.life.map(l => List(l.toByte)).getOrElse(Nil)),
          PropertyValue(property = CardProperties.durability, tinyintValues = card.durability.map(d => List(d.toByte)).getOrElse(Nil)),
          PropertyValue(property = CommonProperties.isGATCGCard, booleanValues = List(true))
        )
      ))))
      // Generate Collections containing EditionProperties
      val editions = cards.flatMap(_.editions)
      val editionsMap = Map.from(editions.map(edition => (edition.uuid, Collection(
        pk = existingEditions.getOrElse(edition.uuid, UUID.randomUUID),
        virtual = true,
        propertyValues = List(
          PropertyValue(property = EditionProperties.uuid, varcharValues = List(edition.uuid)),
          PropertyValue(property = EditionProperties.cardId, varcharValues = List(edition.card_id)),
          PropertyValue(property = EditionProperties.collectorNumber, varcharValues = List(edition.collector_number)),
          PropertyValue(property = EditionProperties.illustrator, varcharValues = List(edition.illustrator)),
          PropertyValue(property = EditionProperties.slug, varcharValues = List(edition.slug)),
          PropertyValue(property = EditionProperties.rarity, tinyintValues = List(edition.rarity.toByte)),
          PropertyValue(property = EditionProperties.effect, varcharValues = edition.effect.map(List(_)).getOrElse(Nil)),
          PropertyValue(property = EditionProperties.flavourText, varcharValues = edition.flavor.map(List(_)).getOrElse(Nil)),
          PropertyValue(property = CommonProperties.isGATCGEdition, booleanValues = List(true))
        )
      ))))
      // Generate Card Collections that will be configured as children of Set Collections and source their Properties and PropertyValues from CardProperties and EditionProperties collections
      val compositeCardsMap = Map.from(
        editions.map(edition => (
          (setMap.get((edition.set.prefix, edition.set.language)), editionsMap.get(edition.uuid), cardsMap.get(edition.card_id)),
          Collection(
            // TODO: We need to find a way to link existing PKs for these
            virtual = true
            // TODO: We need to set PropertyValues on these
          )
        )
      ))
      // Generate Relationships
      val relationships = compositeCardsMap.flatMap {
        case ((Some(set), Some(edition), Some(card)), compositeCard) => List(
          Relationship(collectionPK = compositeCard.pk, relatedCollectionPK = set.pk, relationshipType = ParentCollection),
          Relationship(collectionPK = compositeCard.pk, relatedCollectionPK = edition.pk, relationshipType = SourceOfPropertiesAndPropertyValues),
          Relationship(collectionPK = compositeCard.pk, relatedCollectionPK = card.pk, relationshipType = SourceOfPropertiesAndPropertyValues)
        )
        case _ =>
          logger.warn("This should never happen")
          Nil
      }
      val distinctRelationships = relationships.toList.distinctBy(relationship => (relationship.collectionPK, relationship.relatedCollectionPK, relationship.relationshipType))
      // Write data
      Injection.produceRun(config.datasourceUri) {
        (collectionDAO: dao.projected.CollectionDAO, relationshipDAO: dao.raw.RelationshipDAO) =>
          collectionDAO.createOrUpdateCollections(
            setMap.values.toList ++ editionsMap.values.toList ++ cardsMap.values.toList ++ compositeCardsMap.values.toList
          )
          relationshipDAO.createOrUpdateRelationships(distinctRelationships)
      }

    })
    // TODO: process coerced data
//    val result = data.map(data => {
//    })
    // TODO: Replace with a real response
    "Something".asJson

  def downloadDataset(config: Config) =
    logger.info("Downloading GATCG dataaset")
    val result = getData() match
      case Failure(e) => DownloadDatasetResult(downloadSucceeded = false, errorMessage = Some(e.getMessage))
      case Success(data) =>
        logger.info(s"Downloaded ${data.length} GATCG cards")
        val path = getSubconfigFromConfig(config).grandArchiveTCGJSON.map(f => os.FilePath(f)).getOrElse(defaultDatasetPath)
        val dataAsString = data.asJson.spaces2
        path match
          case p: os.Path => os.write(p, dataAsString)
          case p: os.RelPath => os.write(os.pwd / p, dataAsString)
          case p: os.SubPath => os.write(os.pwd / p, dataAsString)
        DownloadDatasetResult(datasetPath = Some(path.toString), datasetSize = data.length)
    result.asJson

object GATCGCLIPlugin extends PluginDef:
  many[CLIPlugin].add(GATCGCLIPlugin())
  many[Plugin].add(GATCGCLIPlugin())
