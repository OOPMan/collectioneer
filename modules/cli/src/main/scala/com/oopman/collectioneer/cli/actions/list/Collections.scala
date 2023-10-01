package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.dao.CollectionsDAO
import com.oopman.collectioneer.db.dao.projected.PropertyValuesDAO
import com.oopman.collectioneer.db.entity.Collections
import com.oopman.collectioneer.db.entity.projected.PropertyValues
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.time.ZonedDateTime
import java.util.UUID


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
  collections: List[Collections]
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

sealed trait PropertyValue
case class PropertyValueVarchar(name: String, value: List[String]) extends PropertyValue
case class PropertyValueInt(name: String, value: List[Int]) extends PropertyValue

case class CollectionWithPropertyValues
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  properties: List[PropertyValues]
)

case class GetCollectionsResult
(
  dataSourceUri: String,
  count: Int,
  collections: List[CollectionWithPropertyValues]
)

def getCollections(config: Config): Json =
  val collectionsDAO = new CollectionsDAO(config.datasourceUri)
  val propertyValuesDAO = new PropertyValuesDAO(config.datasourceUri)
  val collections = collectionsDAO.getAllMatchingPKs(config.uuids)
  val propertyValues = propertyValuesDAO.getPropertyValuesByPropertyValueSet(config.uuids)
  val propertyValuesByPVSUUID = propertyValues.groupBy(_.propertyValueSetPk)
  collections
    .map(collection => CollectionWithPropertyValues(
      pk = collection.pk,
      virtual = collection.virtual,
      deleted = collection.deleted,
      created = collection.created,
      modified = collection.modified,
      properties = propertyValuesByPVSUUID.getOrElse(collection.pk, Nil)
    )).asJson