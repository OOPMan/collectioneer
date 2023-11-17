package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.queries.h2
import scalikejdbc.*
import java.sql.Connection

object PropertyDAO:

  def createProperties(properties: Seq[entity.Property])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyQueries
      .insert
      .batch(entity.Property.propertiesSeqToBatchInsertSeqSeq(properties): _*)
      .apply()

  def createOrUpdateProperties(properties: Seq[entity.Property])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyQueries
      .upsert
      .batch(entity.Property.propertiesSeqToBatchUpsertSeqSeq(properties): _*)
      .apply()

  def getAll()(implicit session: DBSession = AutoSession): List[entity.raw.Property] =
    h2.raw.PropertyQueries
      .selectAll
      .map(entity.raw.Property(entity.raw.p1.resultName))
      .list
      .apply()


class PropertyDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def createProperties(properties: Seq[entity.Property]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyDAO.createProperties(properties) }

  def createOrUpdateProperties(properties: Seq[entity.Property]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyDAO.createOrUpdateProperties(properties) }

  def getAll: List[entity.raw.Property] =
    dbProvider() readOnly { implicit session => PropertyDAO.getAll() }
  