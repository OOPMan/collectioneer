package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.queries.h2
import scalikejdbc.*

import java.sql.Connection
import java.util.UUID


object CollectionDAO:
  def createCollections(collections: List[entity.Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.CollectionQueries
      .insert
      .batch(entity.Collection.collectionsListToBatchInsertSeqList(collections): _*)
      .apply()

  def createOrUpdateCollections(collections: List[entity.Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.CollectionQueries
      .upsert
      .batch(entity.Collection.collectionsListToBatchUpsertSeqList(collections): _*)
      .apply()

  def getAll(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    h2.raw.CollectionQueries
      .all
      .map(entity.raw.Collection(entity.raw.c1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    h2.raw.CollectionQueries
      .allMatchingPKs(collectionPKs)
      .map(entity.raw.Collection(entity.raw.c1.resultName))
      .list
      .apply()

class CollectionDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def createCollections(collections: List[entity.Collection]): Array[Int] =
    dbProvider() localTx { implicit session => CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: List[entity.Collection]): Array[Int] =
    dbProvider() localTx { implicit session => CollectionDAO.createOrUpdateCollections(collections) }

  def getAll: List[entity.raw.Collection] =
    dbProvider() readOnly { implicit session => CollectionDAO.getAll }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[entity.raw.Collection] =
    dbProvider() readOnly { implicit session => CollectionDAO.getAllMatchingPKs(collectionPKs) }
