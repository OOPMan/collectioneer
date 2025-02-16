package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.SortDirection
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, Property}

import java.util.UUID

trait CollectionDAO:
  def createCollections(collections: Seq[Collection]): Array[Int]
  def createOrUpdateCollections(collections: Seq[Collection]): Array[Int]
  def getAll: List[Collection]
  def getAllMatchingConstraints(comparisons: Seq[Comparison] = Nil,
                                collectionPKs: Option[Seq[UUID]] = None,
                                parentCollectionPKs: Option[Seq[UUID]] = None,
                                sortProperties: Seq[(Property, SortDirection)] = Nil,
                                offset: Option[Int] = None,
                                limit: Option[Int] = None): List[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collection]
  def getAllMatchingPropertyValues(comparisons: Seq[Comparison]): List[Collection]
  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison]): List[Collection]