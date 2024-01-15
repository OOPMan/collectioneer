package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Collection
import com.oopman.collectioneer.db.{DBConnectionProvider, entity, traits}
import scalikejdbc.*

import java.util.UUID

class CollectionDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def createCollections(collections: Seq[Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: Seq[Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createOrUpdateCollections(collections) }

  def getAll: List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAll }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingPKs(collectionPKs) }

