package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Collection
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID


object CollectionDAO extends traits.dao.raw.CollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.CollectionQueries
      .insert
      .batch(traits.entity.raw.Collection.collectionsSeqToBatchInsertSeq(collections): _*)
      .apply()

  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.CollectionQueries
      .upsert
      .batch(traits.entity.raw.Collection.collectionsSeqToBatchInsertSeq(collections): _*)
      .apply()

  def getAll(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    postgresbackend.queries.raw.CollectionQueries
      .all
      .map(postgresbackend.entity.raw.Collection(postgresbackend.entity.raw.Collection.c1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    postgresbackend.queries.raw.CollectionQueries
      .allMatchingPKs(collectionPKs)
      .map(postgresbackend.entity.raw.Collection(postgresbackend.entity.raw.Collection.c1.resultName))
      .list
      .apply()

