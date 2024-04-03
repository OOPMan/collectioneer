package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.DatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipCollection

import java.util.UUID

class RelationshipCollectionDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend) extends traits.dao.raw.RelationshipCollectionDAO:
  def createRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipCollectionDAO.createRelationshipCollections(relationshipCollections) }
  def createOrUpdateRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipCollectionDAO.createOrUpdateRelationshipCollections(relationshipCollections) }

  def deleteRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipCollectionDAO.deleteRelationshipCollections(relationshipCollections) }
  def getAllMatchingRelationshipPKs(relationshipPKs: Seq[UUID]): List[RelationshipCollection] =
    dbProvider() readOnly { implicit session => db.dao.raw.RelationshipCollectionDAO.getAllMatchingRelationshipPKs(relationshipPKs) }
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): List[RelationshipCollection] =
    dbProvider() readOnly { implicit session => db.dao.raw.RelationshipCollectionDAO.getAllMatchingCollectionPKs(collectionPKs) }
