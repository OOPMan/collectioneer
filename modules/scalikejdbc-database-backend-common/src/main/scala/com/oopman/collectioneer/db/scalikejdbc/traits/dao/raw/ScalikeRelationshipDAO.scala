package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.{Relationship, RelationshipType}
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikeRelationshipDAO:
  def createRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int]
  def createOrUpdateRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int]
  def deleteRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int]
  def getRelationshipsByCollectionPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType])(implicit session: DBSession): List[Relationship]
  def getRelationshipsByRelatedCollectionPKsAndRelationshipTypes(relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType])(implicit session: DBSession): List[Relationship]
  
