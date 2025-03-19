package com.oopman.collectioneer.db.scalikejdbc.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.{Property => RawProperty}

import java.util.UUID

class PropertyDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.projected.PropertyDAO:
  def createProperties(properties: Seq[Property]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.projected.PropertyDAO.createProperties(properties) }
  
  def createOrUpdateProperties(properties: Seq[Property]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.projected.PropertyDAO.createOrUpdateProperties(properties) }

  def getAll: Seq[Property] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyDAO.getAll }

  def getAllMatchingPKs(propertyPKs: Seq[UUID]): Seq[Property] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyDAO.getAllMatchingPKs(propertyPKs) }

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison]): Seq[Property] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyDAO.getAllMatchingPropertyValues(comparisons) }

  def inflateRawProperties(properties: Seq[RawProperty]): Seq[Property] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyDAO.inflateRawProperties(properties) }
