package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.entity
import scalikejdbc._

object PropertyDAO:
  def createProperties(properties: Seq[entity.projected.Property])(implicit session: DBSession = AutoSession) = ???

  def createOrUpdateProperties(properties: Seq[entity.projected.Property])(implicit session: DBSession = AutoSession) = ???

  def getAll()(implicit session: DBSession = AutoSession): List[entity.projected.Property] = ???

class PropertyDAO(val connectionPoolName: String):
  def createProperties(properties: Seq[entity.projected.Property]) = ???

  def createOrUpdateProperties(properties: Seq[entity.projected.Property]) = ???

  def getAll(): List[entity.projected.Property] = ???

