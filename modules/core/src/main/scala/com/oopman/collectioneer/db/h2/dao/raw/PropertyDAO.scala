package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.PropertyQueries
import com.oopman.collectioneer.db.traits.entity.Property
import scalikejdbc.*

import java.sql.Connection

object PropertyDAO:

  def createProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int] =
    PropertyQueries
      .insert
      .batch(traits.entity.Property.propertiesSeqToBatchInsertSeqSeq(properties): _*)
      .apply()

  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int] =
    raw.PropertyQueries
      .upsert
      .batch(traits.entity.Property.propertiesSeqToBatchUpsertSeqSeq(properties): _*)
      .apply()

  def getAll()(implicit session: DBSession = AutoSession): List[entity.raw.Property] =
    raw.PropertyQueries
      .all
      .map(entity.raw.Property(entity.raw.p1.resultName))
      .list
      .apply()


class PropertyDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def createProperties(properties: Seq[Property]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyDAO.createProperties(properties) }

  def createOrUpdateProperties(properties: Seq[Property]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyDAO.createOrUpdateProperties(properties) }

  def getAll: List[entity.raw.Property] =
    dbProvider() readOnly { implicit session => PropertyDAO.getAll() }
  