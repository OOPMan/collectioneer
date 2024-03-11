package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits
import scalikejdbc._

import java.util.UUID

trait CollectionDAO:
  def createCollections(collections: Seq[traits.entity.projected.Collection])(implicit session: DBSession = AutoSession): Array[Int]
  def createOrUpdateCollections(collections: Seq[traits.entity.projected.Collection])(implicit session: DBSession = AutoSession): Array[Int]
  def getAll(implicit session: DBSession = AutoSession): List[traits.entity.projected.Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[traits.entity.projected.Collection]

