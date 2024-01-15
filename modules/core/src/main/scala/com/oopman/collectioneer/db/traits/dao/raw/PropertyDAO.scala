package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Property
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

trait PropertyDAO:
  def createProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int]
  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int]

  def getAll()(implicit session: DBSession = AutoSession): List[Property]