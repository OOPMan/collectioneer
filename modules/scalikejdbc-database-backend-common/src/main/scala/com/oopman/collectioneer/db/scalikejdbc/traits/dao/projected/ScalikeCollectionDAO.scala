package com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.Collection as RawCollection
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikeCollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession): Seq[Int]
  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession): Seq[Int]
  def getAll(propertyPKs: Seq[UUID] = Nil)(implicit session: DBSession): Seq[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID], propertyPKs: Seq[UUID] = Nil)(implicit session: DBSession): Seq[Collection]
  def inflateRawCollections(collections: Seq[RawCollection], propertyPKs: Seq[UUID] = Nil)(implicit session: DBSession): Seq[Collection]
