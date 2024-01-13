package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.{entity, traits, DBConnectionProvider}


class PropertyDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def createProperties(properties: Seq[traits.entity.Property]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createProperties(properties) }

  def createOrUpdateProperties(properties: Seq[traits.entity.Property]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createOrUpdateProperties(properties) }

  def getAll: List[entity.raw.Property] =
    dbProvider() readOnly { implicit session => db.dao.raw.PropertyDAO.getAll() }
