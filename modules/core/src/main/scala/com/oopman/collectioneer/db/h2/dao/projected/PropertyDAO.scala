package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

object PropertyDAO extends traits.dao.projected.PropertyDAO:
  def createProperties(properties: Seq[traits.entity.projected.Property])(implicit session: DBSession = AutoSession) = ???

  def createOrUpdateProperties(properties: Seq[traits.entity.projected.Property])(implicit session: DBSession = AutoSession) = ???

  def getAll()(implicit session: DBSession = AutoSession): List[traits.entity.projected.Property] = ???

class PropertyDAO(val connectionPoolName: String):
  def createProperties(properties: Seq[traits.entity.projected.Property]) = ???

  def createOrUpdateProperties(properties: Seq[traits.entity.projected.Property]) = ???

  def getAll(): List[traits.entity.projected.Property] = ???

