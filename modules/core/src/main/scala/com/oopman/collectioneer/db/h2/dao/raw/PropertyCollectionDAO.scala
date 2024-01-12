package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.PropertyCollectionQueries
import com.oopman.collectioneer.db.traits.entity.PropertyCollection
import scalikejdbc.*

import java.sql.Connection

object PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int] =
    PropertyCollectionQueries
      .insert
      .batch(traits.entity.PropertyCollection.propertyCollectionSeqToBatchInsertSeqSeq(propertyCollections): _*)
      .apply()

  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int] =
    raw.PropertyCollectionQueries
      .upsert
      .batch(traits.entity.PropertyCollection.propertyCollectionSeqToBatchUpsertSeqSeq(propertyCollections): _*)
      .apply()

class PropertyCollectionDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def createPropertyCollections(propertyCollection: Seq[PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyCollectionDAO.createPropertyCollections(propertyCollection) }

  def createOrUpdatePropertyCollections(propertyCollection: Seq[PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyCollectionDAO.createOrUpdatePropertyCollections(propertyCollection) }
