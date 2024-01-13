package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.{entity, traits, DBConnectionProvider}
import scalikejdbc.*

import java.util.UUID

class CollectionDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def createCollections(collections: Seq[traits.entity.Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: Seq[traits.entity.Collection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.CollectionDAO.createOrUpdateCollections(collections) }

  def getAll: List[entity.raw.Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAll }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[entity.raw.Collection] =
    dbProvider() readOnly { implicit session => db.dao.raw.CollectionDAO.getAllMatchingPKs(collectionPKs) }

