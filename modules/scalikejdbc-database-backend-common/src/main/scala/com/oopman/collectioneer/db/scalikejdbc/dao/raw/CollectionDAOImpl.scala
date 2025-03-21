package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.{SortDirection, traits}
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, Property}

import java.util.UUID

class CollectionDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.raw.CollectionDAO:

  def createCollections(collections: Seq[Collection]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: Seq[Collection]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createOrUpdateCollections(collections) }

  def getAll: Seq[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAll }

  def getAllMatchingConstraints(comparisons: Seq[Comparison] = Nil,
                                collectionPKs: Option[Seq[UUID]] = None,
                                parentCollectionPKs: Option[Seq[UUID]] = None,
                                sortProperties: Seq[(Property, SortDirection)] = Nil,
                                offset: Option[Int] = None,
                                limit: Option[Int] = None): Seq[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingConstraints(
      comparisons = comparisons, collectionPKs = collectionPKs, parentCollectionPKs = parentCollectionPKs,
      sortProperties = sortProperties, offset = offset, limit = limit)
    }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): Seq[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingPKs(collectionPKs) }

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison]): Seq[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingPropertyValues(comparisons) }

  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison]): Seq[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllRelatedMatchingPropertyValues(comparisons) }