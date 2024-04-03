package com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.Property
import scalikejdbc.DBSession

trait PropertyDAO:
  def createProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def getAll(implicit session: DBSession): List[Property]

