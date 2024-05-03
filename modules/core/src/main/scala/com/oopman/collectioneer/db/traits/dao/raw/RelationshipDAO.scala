package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.{Relationship, RelationshipType}

import java.util.UUID

trait RelationshipDAO:
  def createRelationships(relationships: Seq[Relationship]): Array[Int]
  def createOrUpdateRelationships(relationships: Seq[Relationship]): Array[Int]
  def deleteRelationships(relationships: Seq[Relationship]): Array[Int]
  def getRelationshipsByCollectionPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): List[Relationship]
  def getRelationshipsByRelatedCollectionPKsAndRelationshipTypes(relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): List[Relationship]
  def getRelationshipsByPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]): List[Relationship]

