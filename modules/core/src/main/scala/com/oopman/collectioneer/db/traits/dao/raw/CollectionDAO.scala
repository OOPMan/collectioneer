package com.oopman.collectioneer.db.traits.dao.raw

import scalikejdbc.*
import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.raw.Collection

import java.util.UUID

trait CollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int]
  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int]
  def getAll(implicit session: DBSession = AutoSession): List[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[Collection]
