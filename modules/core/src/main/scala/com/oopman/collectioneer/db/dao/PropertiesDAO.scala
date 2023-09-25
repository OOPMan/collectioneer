package com.oopman.collectioneer.db.dao

import scalikejdbc._
import com.oopman.collectioneer.db.entity.{Properties, p1}
import com.oopman.collectioneer.db.queries.h2.PropertyQueries

object PropertiesDAO:
  def getAll()(implicit session: DBSession = AutoSession): List[Properties] =
    PropertyQueries.all.map(Properties(p1.resultName)).list.apply()


class PropertiesDAO(val connectionPoolName: String):
  
  def getAll(): List[Properties] = NamedDB(connectionPoolName) readOnly { implicit session => PropertiesDAO.getAll() }
  