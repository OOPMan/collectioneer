package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.raw.Property

import java.util.UUID

class PropertyDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend) extends traits.dao.raw.PropertyDAO:

  def createProperties(properties: Seq[Property]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createProperties(properties) }

  def createOrUpdateProperties(properties: Seq[Property]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createOrUpdateProperties(properties) }

  def getAll: List[Property] =
    dbProvider() readOnly { implicit session => db.dao.raw.PropertyDAO.getAll }

  def getAllMatchingPKs(uuids: Seq[UUID]): List[Property] = ???
