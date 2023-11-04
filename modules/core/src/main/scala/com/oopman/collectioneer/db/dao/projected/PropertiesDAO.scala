package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.queries.h2
import scalikejdbc._

object PropertiesDAO:
  def createProperties(properties: List[entity.projected.Property])(implicit session: DBSession = AutoSession) = ???

  def createOrUpdateProperties(properties: List[entity.projected.Property])(implicit session: DBSession = AutoSession) = ???

  def getAll()(implicit session: DBSession = AutoSession): List[entity.projected.Property] = ???

class PropertiesDAO(val connectionPoolName: String):
  def createProperties(properties: List[entity.projected.Property]) = ???

  def createOrUpdateProperties(properties: List[entity.projected.Property]) = ???

  def getAll(): List[entity.projected.Property] = ???

