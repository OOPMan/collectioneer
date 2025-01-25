package com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.Collection
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikeCollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession): Array[Int]
  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession): Array[Int]
  def getAll(propertyPKs: Seq[UUID] = Nil)(implicit session: DBSession): List[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID], propertyPKs: Seq[UUID] = Nil)(implicit session: DBSession): List[Collection]

