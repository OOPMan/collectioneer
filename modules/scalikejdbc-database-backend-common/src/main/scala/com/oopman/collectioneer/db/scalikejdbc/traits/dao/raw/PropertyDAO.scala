package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Property
import scalikejdbc.DBSession

import java.util.UUID

trait PropertyDAO:
  def createProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def getAll(implicit session: DBSession): List[Property]
  def getAllMatchingPKs(uuids: Seq[UUID])(implicit session: DBSession): List[Property]