package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.{traits, entity}
import scalikejdbc._

trait PropertyDAO:
  def createProperties(properties: Seq[traits.entity.Property])(implicit session: DBSession = AutoSession): Array[Int]
  def createOrUpdateProperties(properties: Seq[traits.entity.Property])(implicit session: DBSession = AutoSession): Array[Int]

  def getAll()(implicit session: DBSession = AutoSession): List[entity.raw.Property]