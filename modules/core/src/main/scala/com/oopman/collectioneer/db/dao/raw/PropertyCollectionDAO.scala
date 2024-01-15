package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection
import com.oopman.collectioneer.db.{DBConnectionProvider, entity, traits}

class PropertyCollectionDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def createPropertyCollections(propertyCollection: Seq[PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.createPropertyCollections(propertyCollection) }

  def createOrUpdatePropertyCollections(propertyCollection: Seq[PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.createOrUpdatePropertyCollections(propertyCollection) }
