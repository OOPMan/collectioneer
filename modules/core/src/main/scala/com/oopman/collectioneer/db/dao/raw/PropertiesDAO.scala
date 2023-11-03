package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity.raw.{Property, p1}
import com.oopman.collectioneer.db.queries.h2.raw.PropertyQueries
import scalikejdbc.*

object PropertiesDAO:
  def propertiesListToBatchInsertSeqList(properties: List[Property]) =
    properties.map(p => Seq(
      p.pk.toString,
      p.propertyName,
      p.propertyTypes.map(_.toString).toArray,
      p.deleted,
      p.created,
      p.modified)
    )

  def createProperties(properties: List[Property])(implicit session: DBSession = AutoSession) =
    PropertyQueries
      .insert
      .batch(propertiesListToBatchInsertSeqList(properties): _*)
      .apply()

  def createOrUpdateProperties(properties: List[Property])(implicit session: DBSession = AutoSession) =
    PropertyQueries
      .upsert
      .batch(propertiesListToBatchInsertSeqList(properties): _*)
      .apply()

  def getAll()(implicit session: DBSession = AutoSession): List[Property] =
    PropertyQueries.selectAll.map(Property(p1.resultName)).list.apply()


class PropertiesDAO(val connectionPoolName: String):

  def createProperties(properties: List[Property]) = NamedDB(connectionPoolName) localTx { implicit session =>
    PropertiesDAO.createProperties(properties)
  }

  def createOrUpdateProperties(properties: List[Property]) = NamedDB(connectionPoolName) localTx { implicit session =>
    PropertiesDAO.createOrUpdateProperties(properties)
  }
  
  def getAll(): List[Property] = NamedDB(connectionPoolName) readOnly { implicit session => PropertiesDAO.getAll() }
  