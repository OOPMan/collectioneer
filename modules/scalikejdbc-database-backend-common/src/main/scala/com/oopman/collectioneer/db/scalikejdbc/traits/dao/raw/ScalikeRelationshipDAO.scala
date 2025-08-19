package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.{Relationship, HasTopLevelCollectionPKAndLevel, RelationshipType}
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikeRelationshipDAO:
  def createRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Seq[Int]
  def createOrUpdateRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Seq[Int]
  def deleteRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Seq[Int]
  def getRelationshipsByCollectionPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType])(implicit session: DBSession): Seq[Relationship]
  def getRelationshipsByRelatedCollectionPKsAndRelationshipTypes(relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType])(implicit session: DBSession): Seq[Relationship]
  def getRelationshipsByPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType])(implicit session: DBSession): Seq[Relationship]
  def getRelationshipHierarchyByCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): Seq[Relationship & HasTopLevelCollectionPKAndLevel]
