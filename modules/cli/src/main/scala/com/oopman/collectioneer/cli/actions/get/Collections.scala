package com.oopman.collectioneer.cli.actions.get

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.cli.actions.get.Properties.{PropertyWithPropertyValues, propertyToPropertyWithPropertyValues}
import com.oopman.collectioneer.db.{Injection, traits}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.time.ZonedDateTime
import java.util.UUID

object Collections:
  case class CollectionWithPropertyValues
  (
    pk: UUID = UUID.randomUUID(),
    virtual: Boolean = false,
    deleted: Boolean = false,
    created: ZonedDateTime = ZonedDateTime.now(),
    modified: ZonedDateTime = ZonedDateTime.now(),
    properties: List[PropertyWithPropertyValues],
    propertyValues: Map[String, List[String]]
  )

  case class GetCollectionsResult
  (
    dataSourceUri: String,
    count: Int,
    collections: List[CollectionWithPropertyValues]
  )

  
  def getCollections(config: Config): Json =
    def getCollections(collectionDAO: traits.dao.projected.CollectionDAO) =
      val collections = collectionDAO.getAllMatchingPKs(config.uuids)
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
            properties = collection.properties.map(propertyToPropertyWithPropertyValues),
            propertyValues = Map
              .from(collection.propertyValues.map(Common.propertyValuesToMapTuple))
          ))).asJson
    Injection.produceRun(config)(getCollections)