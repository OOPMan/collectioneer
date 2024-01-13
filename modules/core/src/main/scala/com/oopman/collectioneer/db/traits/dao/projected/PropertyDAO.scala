package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.entity
import scalikejdbc._

trait PropertyDAO:
  def createProperties(properties: Seq[entity.projected.Property])(implicit session: DBSession = AutoSession): Array[Int]

  def createOrUpdateProperties(properties: Seq[entity.projected.Property])(implicit session: DBSession = AutoSession): Array[Int]

  def getAll()(implicit session: DBSession = AutoSession): List[entity.projected.Property]

