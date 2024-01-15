package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Property
import com.oopman.collectioneer.db.{DBConnectionProvider, entity, traits}


class PropertyDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def createProperties(properties: Seq[Property]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createProperties(properties) }

  def createOrUpdateProperties(properties: Seq[Property]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createOrUpdateProperties(properties) }

  def getAll: List[Property] =
    dbProvider() readOnly { implicit session => db.dao.raw.PropertyDAO.getAll() }
