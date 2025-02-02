package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.{SortDirection, traits}
import com.oopman.collectioneer.db.traits.entity.raw.Collection

import java.util.UUID

class CollectionDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.raw.CollectionDAO:

  def createCollections(collections: Seq[Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: Seq[Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createOrUpdateCollections(collections) }

  def getAll: List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAll }

  def getAllMatchingConstraints(comparisons: Seq[Comparison] = Nil,
                                collectionPKs: Option[Seq[UUID]] = None,
                                parentCollectionPKs: Option[Seq[UUID]] = None,
                                sortPropertyPKs: Seq[(UUID, SortDirection)] = Nil,
                                offset: Option[Int] = None,
                                limit: Option[Int] = None): List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingConstraints(
      comparisons = comparisons, collectionPKs = collectionPKs, parentCollectionPKs = parentCollectionPKs,
      sortPropertyPKs = sortPropertyPKs, offset = offset, limit = limit)
    }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingPKs(collectionPKs) }

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison]): List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingPropertyValues(comparisons) }

  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison]): List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllRelatedMatchingPropertyValues(comparisons) }