package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.{Relationship, HasTopLevelCollectionPKAndLevel, RelationshipType}

import java.util.UUID

trait RelationshipDAO:
  def createRelationships(relationships: Seq[Relationship]): Seq[Int]
  def createOrUpdateRelationships(relationships: Seq[Relationship]): Seq[Int]
  def deleteRelationships(relationships: Seq[Relationship]): Seq[Int]
  def getRelationshipsByCollectionPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): Seq[Relationship]
  def getRelationshipsByRelatedCollectionPKsAndRelationshipTypes(relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): Seq[Relationship]
  def getRelationshipsByPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): Seq[Relationship]
  def getRelationshipHierarchyByCollectionPKs(collectionPKs: Seq[UUID]): Seq[Relationship & HasTopLevelCollectionPKAndLevel]

