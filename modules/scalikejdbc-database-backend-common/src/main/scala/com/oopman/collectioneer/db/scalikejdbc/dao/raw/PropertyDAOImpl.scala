package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.traits.entity.raw.Property
import com.oopman.collectioneer.db.{PropertyValueQueryDSL, traits}

import java.util.UUID

class PropertyDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.raw.PropertyDAO:

  def createProperties(properties: Seq[Property]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createProperties(properties) }

  def createOrUpdateProperties(properties: Seq[Property]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyDAO.createOrUpdateProperties(properties) }

  def getAll: Seq[Property] =
    dbProvider() readOnly { implicit session => db.dao.raw.PropertyDAO.getAll }

  def getAllMatchingPKs(propertyPKs: Seq[UUID]): Seq[Property] =
    dbProvider() readOnly { implicit session => db.dao.raw.PropertyDAO.getAllMatchingPKs(propertyPKs) }

  def getAllMatchingPropertyValues(comparisons: Seq[PropertyValueQueryDSL.Comparison]): Seq[Property] =
    dbProvider() readOnly { implicit session => db.dao.raw.PropertyDAO.getAllMatchingPropertyValues(comparisons) }



