package com.oopman.collectioneer.db.scalikejdbc.dao.projected

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.DatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.projected.Collection

import java.util.UUID

class CollectionDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend) extends traits.dao.projected.CollectionDAO:
  def createCollections(collections: Seq[Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.projected.CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: Seq[Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.projected.CollectionDAO.createOrUpdateCollections(collections) }

  def getAll: List[Collection] = ???

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collection] = ???

  