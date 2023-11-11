package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.queries.h2
import scalikejdbc.*

import java.sql.Connection

object PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: List[entity.PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyCollectionQueries
      .insert
      .batch(entity.PropertyCollection.propertyCollectionListToBatchInserSeqList(propertyCollections): _*)
      .apply()

  def createOrUpdatePropertyCollections(propertyCollections: List[entity.PropertyCollection])(implicit  session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyCollectionQueries
      .upsert
      .batch(entity.PropertyCollection.propertyCollectionListToBatchUpsertSeqList(propertyCollections): _*)
      .apply()

class PropertyCollectionDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))
