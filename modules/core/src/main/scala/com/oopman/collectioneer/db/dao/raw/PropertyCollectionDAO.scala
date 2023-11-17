package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.queries.h2
import scalikejdbc.*

import java.sql.Connection

object PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[entity.PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyCollectionQueries
      .insert
      .batch(entity.PropertyCollection.propertyCollectionSeqToBatchInsertSeqSeq(propertyCollections): _*)
      .apply()

  def createOrUpdatePropertyCollections(propertyCollections: Seq[entity.PropertyCollection])(implicit  session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyCollectionQueries
      .upsert
      .batch(entity.PropertyCollection.propertyCollectionSeqToBatchUpsertSeqSeq(propertyCollections): _*)
      .apply()

class PropertyCollectionDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def createPropertyCollections(propertyCollection: Seq[entity.PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyCollectionDAO.createPropertyCollections(propertyCollection) }

  def createOrUpdatePropertyCollections(propertyCollection: Seq[entity.PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyCollectionDAO.createOrUpdatePropertyCollections(propertyCollection) }
