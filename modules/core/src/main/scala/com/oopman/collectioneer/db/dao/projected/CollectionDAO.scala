package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.{DBConnectionProvider, entity, traits}
import scalikejdbc._

class CollectionDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):
  def createCollections(collections: Seq[traits.entity.projected.Collection]) =
    dbProvider() localTx { implicit session => db.dao.projected.CollectionDAO.createCollections(collections) }

  def createOrUpdateCollections(collections: Seq[traits.entity.projected.Collection]) = ???
