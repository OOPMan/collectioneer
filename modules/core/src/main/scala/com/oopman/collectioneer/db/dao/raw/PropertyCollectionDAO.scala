package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.{entity, traits, DBConnectionProvider}

class PropertyCollectionDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def createPropertyCollections(propertyCollection: Seq[traits.entity.PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.createPropertyCollections(propertyCollection) }

  def createOrUpdatePropertyCollections(propertyCollection: Seq[traits.entity.PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.createOrUpdatePropertyCollections(propertyCollection) }
