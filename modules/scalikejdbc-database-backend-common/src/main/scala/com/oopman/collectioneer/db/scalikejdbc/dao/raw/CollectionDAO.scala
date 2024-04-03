package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.raw.Collection

import java.util.UUID

class CollectionDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend) extends traits.dao.raw.CollectionDAO:

  def createCollections(collections: Seq[Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: Seq[Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createOrUpdateCollections(collections) }

  def getAll: List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAll }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingPKs(collectionPKs) }

