package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

trait PropertyDAO:
  def createProperties(properties: Seq[traits.entity.projected.Property])(implicit session: DBSession = AutoSession): Array[Int]

  def createOrUpdateProperties(properties: Seq[traits.entity.projected.Property])(implicit session: DBSession = AutoSession): Array[Int]

  def getAll()(implicit session: DBSession = AutoSession): List[traits.entity.projected.Property]

