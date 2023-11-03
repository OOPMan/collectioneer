package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.queries.h2
import scalikejdbc.*

object PropertiesDAO:

  def createProperties(properties: List[entity.Property])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyQueries
      .insert
      .batch(entity.Property.propertiesListToBatchInsertSeqList(properties): _*)
      .apply()

  def createOrUpdateProperties(properties: List[entity.Property])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyQueries
      .upsert
      .batch(entity.Property.propertiesListToBatchInsertSeqList(properties): _*)
      .apply()

  def getAll()(implicit session: DBSession = AutoSession): List[entity.Property] =
    h2.raw.PropertyQueries
      .selectAll
      .map(entity.raw.Property(entity.raw.p1.resultName))
      .list
      .apply()


class PropertiesDAO(val connectionPoolName: String):

  def createProperties(properties: List[entity.Property]): Array[Int] = NamedDB(connectionPoolName) localTx { implicit session =>
    PropertiesDAO.createProperties(properties)
  }

  def createOrUpdateProperties(properties: List[entity.Property]): Array[Int] = NamedDB(connectionPoolName) localTx { implicit session =>
    PropertiesDAO.createOrUpdateProperties(properties)
  }
  
  def getAll: List[entity.Property] = NamedDB(connectionPoolName) readOnly { implicit session => PropertiesDAO.getAll() }
  