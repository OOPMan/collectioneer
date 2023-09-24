package com.oopman.collectioneer.cli

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import com.oopman.collectioneer.db.dao.CollectionsDAO
import com.oopman.collectioneer.db.entity.Collections

import java.util.UUID


val actions: Map[(Option[Verbs], Option[Subjects]), Config => Json] = Map(
  (Some(Verbs.list), Some(Subjects.collections)) -> listCollections
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
  collections: List[Collections]
)

def listCollections(config: Config) =
  val collectionsDAO = new CollectionsDAO(config.datasourceUri)
  val collections = collectionsDAO.getAll()
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
