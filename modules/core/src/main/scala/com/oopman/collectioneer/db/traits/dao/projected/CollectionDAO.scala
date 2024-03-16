package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.Collection
import scalikejdbc.DBSession

import java.util.UUID

trait CollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession): Array[Int]
  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession): Array[Int]
  def getAll(implicit session: DBSession): List[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): List[Collection]

