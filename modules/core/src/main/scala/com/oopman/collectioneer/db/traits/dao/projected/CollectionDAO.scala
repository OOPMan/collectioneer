package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.Collection

import java.util.UUID

trait CollectionDAO:
  def createCollections(collections: Seq[Collection]): Array[Int]
  def createOrUpdateCollections(collections: Seq[Collection]): Array[Int]
  def getAll(propertyPKs: Seq[UUID] = Nil): List[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID], propertyPKs: Seq[UUID] = Nil): List[Collection]

