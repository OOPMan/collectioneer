package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.SortDirection
import com.oopman.collectioneer.db.traits.entity.raw.Collection

import java.util.UUID

trait CollectionDAO:
  def createCollections(collections: Seq[Collection]): Array[Int]
  def createOrUpdateCollections(collections: Seq[Collection]): Array[Int]
  def getAll: List[Collection]
  def getAll(comparisons: Seq[Comparison] = Nil,
             sortPropertyPKs: Seq[(UUID, SortDirection)] = Nil,
             parentCollectionPKs: Option[Seq[UUID]] = None,
             offset: Option[Int] = None,
             limit: Option[Int] = None): List[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collection]
  def getAllMatchingPropertyValues(comparisons: Seq[Comparison]): List[Collection]
  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison]): List[Collection]