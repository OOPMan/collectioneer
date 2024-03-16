package com.oopman.collectioneer.db.dao.raw
import com.oopman.collectioneer.db.DBConnectionProvider
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection

import java.util.UUID

class PropertyCollectionDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend):
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