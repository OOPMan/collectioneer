package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc._

object PropertyDAO extends traits.dao.projected.PropertyDAO:
  def createProperties(properties: Seq[traits.entity.projected.Property])(implicit session: DBSession = AutoSession) = ???

  def createOrUpdateProperties(properties: Seq[traits.entity.projected.Property])(implicit session: DBSession = AutoSession) = ???

  def getAll()(implicit session: DBSession = AutoSession): List[traits.entity.projected.Property] = ???
