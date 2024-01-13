package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.CollectionQueries
import com.oopman.collectioneer.db.traits.entity.Collection
import scalikejdbc.*

import java.sql.Connection
import java.util.UUID


object CollectionDAO extends traits.dao.raw.CollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    CollectionQueries
      .insert
      .batch(traits.entity.Collection.collectionsListToBatchInsertSeqList(collections): _*)
      .apply()

  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    raw.CollectionQueries
      .upsert
      .batch(traits.entity.Collection.collectionsListToBatchUpsertSeqList(collections): _*)
      .apply()

  def getAll(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    raw.CollectionQueries
      .all
      .map(entity.raw.Collection(entity.raw.c1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    raw.CollectionQueries
      .allMatchingPKs(collectionPKs)
      .map(entity.raw.Collection(entity.raw.c1.resultName))
      .list
      .apply()

