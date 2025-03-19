package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection

import java.util.UUID

class PropertyCollectionDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.raw.PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.createPropertyCollections(propertyCollections) }
  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.createOrUpdatePropertyCollections(propertyCollections) }
  def deletePropertyCollections(propertyCollections: Seq[PropertyCollection]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.deletePropertyCollections(propertyCollections) }
  def getAllMatchingPropertyPKs(propertyPKs: Seq[UUID]): Seq[PropertyCollection] =
    dbProvider() readOnly  { implicit session => db.dao.raw.PropertyCollectionDAO.getAllMatchingPropertyPKs(propertyPKs) }
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): Seq[PropertyCollection] =
    dbProvider() readOnly  { implicit session => db.dao.raw.PropertyCollectionDAO.getAllMatchingCollectionPKs(collectionPKs) }