package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.traits.dao.raw.{CollectionDAO, PropertyDAO}
import com.oopman.collectioneer.db.{Injection, traits}
import distage.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.util.UUID

object Collections:
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

  private def listCollections(config: Config)(collectionDAO: traits.dao.raw.CollectionDAO) = collectionDAO.getAll

  private def listCollectionsByPropertyValueQueries(config: Config)(propertyDAO: PropertyDAO, collectionDAO: CollectionDAO) =
    collectionDAO.getAllRelatedMatchingPropertyValues(Common.generatePropertyValueComparisons(propertyDAO, config.propertyValueQueries))

  def listCollectionsAction(config: Config): Json =
    val collections = config.propertyValueQueries match
      case Some(_) => Injection.produceRun(config)(listCollectionsByPropertyValueQueries(config))
      case None => Injection.produceRun(config)(listCollections(config))
    if config.verbose then
      // TODO: Verbose list includes all property values for each collection
      ListCollectionsVerboseResult(
        dataSourceUri = config.datasourceUri,
        count = collections.size,
        collections = collections
      ).asJson
    else
      // TODO: non-verbose list should include UUID and Property Name
      ListCollectionsResult(
        dataSourceUri = config.datasourceUri,
        count = collections.size,
        uuids = collections.map(_.pk)
      ).asJson
