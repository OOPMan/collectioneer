package com.oopman.collectioneer.db.scalikejdbc.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.projected.Property

import java.util.UUID

class PropertyDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.projected.PropertyDAO:
  def createProperties(properties: Seq[Property]) =
    dbProvider() localTx { implicit session => db.dao.projected.PropertyDAO.createProperties(properties) }
  
  def createOrUpdateProperties(properties: Seq[Property]) =
    dbProvider() localTx { implicit session => db.dao.projected.PropertyDAO.createOrUpdateProperties(properties) }

  def getAll =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyDAO.getAll }

  def getAllMatchingPKs(propertyPKs: Seq[UUID]): List[Property] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyDAO.getAllMatchingPKs(propertyPKs) }

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison]): List[Property] =
    dbProvider() readOnly { implicit session=> db.dao.projected.PropertyDAO.getAllMatchingPropertyValues(comparisons) }
