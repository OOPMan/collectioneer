package com.oopman.collectioneer.db.scalikejdbc.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.{Relationship, RelationshipType}
import scalikejdbc.DBSession

import java.util.UUID

class RelationshipDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.raw.RelationshipDAO:
  def createRelationships(relationships: Seq[Relationship]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipDAO.createRelationships(relationships) }
  def createOrUpdateRelationships(relationships: Seq[Relationship]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipDAO.createOrUpdateRelationships(relationships) }
  def deleteRelationships(relationships: Seq[Relationship]): Seq[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.RelationshipDAO.deleteRelationships(relationships) }
  def getRelationshipsByCollectionPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): Seq[Relationship] =
    dbProvider() readOnly { implicit session => db.dao.raw.RelationshipDAO.getRelationshipsByCollectionPKsAndRelationshipTypes(collectionPKs, relationshipTypes) }
  def getRelationshipsByRelatedCollectionPKsAndRelationshipTypes(relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): Seq[Relationship] =
    dbProvider() readOnly { implicit session => db.dao.raw.RelationshipDAO.getRelationshipsByRelatedCollectionPKsAndRelationshipTypes(relatedCollectionPKs, relationshipTypes) }
  def getRelationshipsByPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): Seq[Relationship] =
    dbProvider() readOnly { implicit session => db.dao.raw.RelationshipDAO.getRelationshipsByPKsAndRelationshipTypes(collectionPKs, relatedCollectionPKs, relationshipTypes) }
