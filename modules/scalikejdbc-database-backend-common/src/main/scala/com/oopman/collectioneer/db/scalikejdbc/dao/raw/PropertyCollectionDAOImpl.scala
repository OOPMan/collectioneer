package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.DatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection

import java.util.UUID

class PropertyCollectionDAOImpl(val dbProvider: DBConnectionProvider, val db: DatabaseBackend) extends traits.dao.raw.PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.createPropertyCollections(propertyCollections) }
  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.createOrUpdatePropertyCollections(propertyCollections) }
  def deletePropertyCollections(propertyCollections: Seq[PropertyCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyCollectionDAO.deletePropertyCollections(propertyCollections) }
  def getAllMatchingPropertyPKs(propertyPKs: Seq[UUID]): List[PropertyCollection] =
    dbProvider() readOnly  { implicit session => db.dao.raw.PropertyCollectionDAO.getAllMatchingPropertyPKs(propertyPKs) }
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): List[PropertyCollection] =
    dbProvider() readOnly  { implicit session => db.dao.raw.PropertyCollectionDAO.getAllMatchingCollectionPKs(collectionPKs) }