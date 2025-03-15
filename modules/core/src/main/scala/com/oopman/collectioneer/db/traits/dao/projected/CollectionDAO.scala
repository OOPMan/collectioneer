package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.Collection as RawCollection

import java.util.UUID

trait CollectionDAO:
  def createCollections(collections: Seq[Collection]): Array[Int]
  def createOrUpdateCollections(collections: Seq[Collection]): Array[Int]
  def getAll(propertyPKs: Seq[UUID] = Nil): List[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID], propertyPKs: Seq[UUID] = Nil): List[Collection]
  def inflateRawCollections(collections: Seq[RawCollection], propertyPKs: Seq[UUID] = Nil): List[Collection]
