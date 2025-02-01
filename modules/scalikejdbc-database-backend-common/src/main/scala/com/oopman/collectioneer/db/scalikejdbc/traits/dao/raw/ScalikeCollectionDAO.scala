package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.SortDirection
import com.oopman.collectioneer.db.traits.entity.raw.Collection
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikeCollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession): Array[Int]
  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession): Array[Int]
  def getAll(implicit session: DBSession): List[Collection]
  def getAll(comparisons: Seq[Comparison] = Nil,
             sortPropertyPKs: Seq[(UUID, SortDirection)] = Nil,
             parentCollectionPKs: Option[Seq[UUID]] = None,
             offset: Option[Int] = None,
             limit: Option[Int] = None)(implicit session: DBSession): List[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): List[Collection]
  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession): List[Collection]
  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession): List[Collection]