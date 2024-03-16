package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.DBConnectionProvider
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.raw.Property

class PropertyDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend):

  def createProperties(properties: Seq[Property]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createProperties(properties) }

  def createOrUpdateProperties(properties: Seq[Property]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createOrUpdateProperties(properties) }

  def getAll: List[Property] =
    dbProvider() readOnly { implicit session => db.dao.raw.PropertyDAO.getAll }
