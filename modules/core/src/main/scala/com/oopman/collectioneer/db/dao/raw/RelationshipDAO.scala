package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.DBConnectionProvider
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.raw.Relationship
import scalikejdbc.DBSession

import java.util.UUID

class RelationshipDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend):
  def createRelationships(relationships: Seq[Relationship]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipDAO.createRelationships(relationships) }
  def createOrUpdateRelationships(relationships: Seq[Relationship]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipDAO.createOrUpdateRelationships(relationships) }
  def deleteRelationships(relationships: Seq[Relationship]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipDAO.deleteRelationships(relationships) }
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): List[Relationship] =
    dbProvider() readOnly { implicit session => db.dao.raw.RelationshipDAO.getAllMatchingCollectionPKs(collectionPKs) }
  def getAllMatchingRelatedCollectionPKs(relatedCollectionPKs: Seq[UUID]): List[Relationship] =
    dbProvider() readOnly { implicit session => db.dao.raw.RelationshipDAO.getAllMatchingRelatedCollectionPKs(relatedCollectionPKs) }
