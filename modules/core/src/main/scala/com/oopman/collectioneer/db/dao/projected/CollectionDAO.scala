package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.DBConnectionProvider
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.projected.Collection

class CollectionDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend):
  def createCollections(collections: Seq[Collection]) =
    dbProvider() localTx { implicit session => db.dao.projected.CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: Seq[Collection]) = ???
