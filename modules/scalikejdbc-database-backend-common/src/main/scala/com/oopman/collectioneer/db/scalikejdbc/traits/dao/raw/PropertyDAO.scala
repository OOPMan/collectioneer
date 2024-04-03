package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Property
import scalikejdbc.DBSession

trait PropertyDAO:
  def createProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def getAll(implicit session: DBSession): List[Property]

// Temporarily commented out as it was breaking compilcation of postgres backend for some unknown reason
//  def getAllMatchingPKs(uuids: Seq[UUID])(implicit session: DBSession): List[Property]