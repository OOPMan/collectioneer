package com.oopman.collectioneer.db.dao

import scalikejdbc._
import com.oopman.collectioneer.db.entity.{Property, p1}
import com.oopman.collectioneer.db.queries.h2.PropertyQueries

object PropertiesDAO:

  def createProperties(properties: List[Property])(implicit session: DBSession = AutoSession) =
    val propertiesSeq = properties.map(p => Seq(
      p.pk.toString,
      p.propertyName,
      p.propertyTypes.map(_.toString).toArray,
      p.deleted,
      p.created,
      p.modified))
    PropertyQueries
      .insert
      .batch(propertiesSeq: _*)
      .apply()

  def getAll()(implicit session: DBSession = AutoSession): List[Property] =
    PropertyQueries.selectAll.map(Property(p1.resultName)).list.apply()


class PropertiesDAO(val connectionPoolName: String):

  def createProperties(properties: List[Property]) = NamedDB(connectionPoolName) localTx { implicit  session =>
    PropertiesDAO.createProperties(properties)
  }
  
  def getAll(): List[Property] = NamedDB(connectionPoolName) readOnly { implicit session => PropertiesDAO.getAll() }
  