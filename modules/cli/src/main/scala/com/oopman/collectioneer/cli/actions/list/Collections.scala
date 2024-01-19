package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.{Injection, traits}
import distage.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import izumi.fundamentals.platform.functional.Identity

import java.time.ZonedDateTime
import java.util.{HexFormat, UUID}

implicit val encodeCollection: Encoder[traits.entity.raw.Collection] = (c: traits.entity.raw.Collection) =>
  Json.obj(
    ("pk", Json.fromString(c.pk.toString)),
    ("virtual", Json.fromBoolean(c.virtual)),
    ("deleted", Json.fromBoolean(c.deleted)),
    ("created", Json.fromString(c.created.toString)),
    ("modified", Json.fromString(c.modified.toString))
  )


case class ListCollectionsResult
(
  dataSourceUri: String,
  count: Int,
  uuids: List[UUID]
)

case class ListCollectionsVerboseResult
(
  dataSourceUri: String,
  count: Int,
  collections: List[traits.entity.raw.Collection]
)

def listCollections(config: Config) =
  def listCollections(collectionDAO: traits.dao.raw.CollectionDAO) =
    val collections = collectionDAO.getAll
    config.verbose match
      case true => ListCollectionsVerboseResult(
        dataSourceUri = config.datasourceUri,
        count = collections.size,
        collections = collections
      ).asJson
      case false => ListCollectionsResult(
        dataSourceUri = config.datasourceUri,
        count = collections.size,
        uuids = collections.map(_.pk)
      ).asJson
  Injection.produceRun(config.datasourceUri)(listCollections.asInstanceOf[Functoid[Identity[Json]]])

case class CollectionWithPropertyValues
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  properties: Map[String, List[String]]
)

case class GetCollectionsResult
(
  dataSourceUri: String,
  count: Int,
  collections: List[CollectionWithPropertyValues]
)

def propertyValuesToMapTuple(propertyValue: traits.entity.projected.PropertyValue): (String, List[String]) =
  val hexFormat = HexFormat.of()
  propertyValue.property.propertyName -> (
    propertyValue.varcharValues ++
    propertyValue.varbinaryValues.map(hexFormat.formatHex) ++
    propertyValue.tinyintValues.map(_.toString) ++
    propertyValue.smallintValues.map(_.toString) ++
    propertyValue.intValues.map(_.toString) ++
    propertyValue.bigintValues.map(_.toString()) ++
    propertyValue.numericValues.map(_.toString()) ++
    propertyValue.floatValues.map(_.toString) ++
    propertyValue.doubleValues.map(_.toString) ++
    propertyValue.booleanValues.map(_.toString) ++
    propertyValue.dateValues.map(_.toString) ++
    propertyValue.timeValues.map(_.toString) ++
    propertyValue.timestampValues.map(_.toString) ++
    propertyValue.clobValues.map(s => s.getSubString(0, s.length().toInt)) ++
    propertyValue.blobValues.map(b => hexFormat.formatHex(b.getBinaryStream.readAllBytes())) ++
    propertyValue.uuidValues.map(_.toString) ++
    propertyValue.jsonValues.map(hexFormat.formatHex)
  )


def getCollections(config: Config): Json =
  def getCollections(collectionDAO: traits.dao.raw.CollectionDAO, propertyValueDAO: traits.dao.projected.PropertyValueDAO) =
    val collections = collectionDAO.getAllMatchingPKs(config.uuids)
    val propertyValues = propertyValueDAO.getPropertyValuesByPropertyValueSets(config.uuids)
    val propertyValuesByPVSUUID = propertyValues.groupBy(_.propertyValueSetPk)
    GetCollectionsResult(
      dataSourceUri = config.datasourceUri,
      count = collections.size,
      collections = collections
        .map(collection => CollectionWithPropertyValues(
          pk = collection.pk,
          virtual = collection.virtual,
          deleted = collection.deleted,
          created = collection.created,
          modified = collection.modified,
          properties = Map.from(propertyValuesByPVSUUID
            .getOrElse(collection.pk, Nil)
            .map(propertyValuesToMapTuple))
        ))).asJson
  Injection.produceRun(config.datasourceUri)(getCollections.asInstanceOf[Functoid[Identity[Json]]])
