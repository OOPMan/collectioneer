package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.dao.CollectionsDAO
import com.oopman.collectioneer.db.dao.projected.PropertyValuesDAO
import com.oopman.collectioneer.db.entity.Collection
import com.oopman.collectioneer.db.entity.projected.PropertyValues
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.time.ZonedDateTime
import java.util.{HexFormat, UUID}


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
  collections: List[Collection]
)

def listCollections(config: Config) =
  val collectionsDAO = new CollectionsDAO(config.datasourceUri)
  val collections = collectionsDAO.getAll
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

def propertyValuesToMapTuple(propertyValue: PropertyValues): (String, List[String]) =
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
  val collectionsDAO = new CollectionsDAO(config.datasourceUri)
  val propertyValuesDAO = new PropertyValuesDAO(config.datasourceUri)
  val collections = collectionsDAO.getAllMatchingPKs(config.uuids)
  val propertyValues = propertyValuesDAO.getPropertyValuesByPropertyValueSet(config.uuids)
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