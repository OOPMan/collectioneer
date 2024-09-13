package com.oopman.collectioneer.cli.actions.get

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.{Injection, traits}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.time.ZonedDateTime
import java.util.{HexFormat, UUID}

object Collections:
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

  
  def getCollections(config: Config): Json =
    def getCollections(collectionDAO: traits.dao.raw.CollectionDAO, propertyValueDAO: traits.dao.projected.PropertyValueDAO) =
      // TODO: use Projected CollectionDAO to retrieve Project Collection objects containing all data
      val collections = collectionDAO.getAllMatchingPKs(config.uuids)
      val propertyValues = propertyValueDAO.getPropertyValuesByCollectionUUIDs(config.uuids)
      val propertyValuesByPVSUUID = propertyValues.groupBy(_.collection.pk)
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
            properties = Map
              .from(propertyValuesByPVSUUID
              .getOrElse(collection.pk, Nil)
              .map(Common.propertyValuesToMapTuple))
          ))).asJson
    Injection.produceRun(config)(getCollections)