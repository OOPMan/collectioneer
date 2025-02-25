package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.cli.{Config, Subconfig, Subject, Verb}
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType
import com.oopman.collectioneer.db.{Injection, entity, traits}
import com.oopman.collectioneer.plugins.CLIPlugin
import com.typesafe.scalalogging.LazyLogging
import distage.ModuleDef
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

  def getDefaultSubconfig: GATCGPluginConfig = GATCGPluginConfig()

  def getSubconfigFromConfig(config: Config): GATCGPluginConfig =
    config.subconfigs.getOrElse(getShortName, getDefaultSubconfig).asInstanceOf[GATCGPluginConfig]

  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Config => Json, List[OParser[?, Config]])] =
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
    val result = modelsTry.map(cards => {
      object importDatasetModule extends ModuleDef:
        make[List[Card]].from(cards)

      Injection.produceRun(config, importDatasetModule)(actions.ImportDataset.importDataset)
    })
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

object GATCGCLIPluginDef extends PluginDef:
  many[CLIPlugin].add[GATCGCLIPlugin]
  many[Plugin].add[GATCGCLIPlugin]
