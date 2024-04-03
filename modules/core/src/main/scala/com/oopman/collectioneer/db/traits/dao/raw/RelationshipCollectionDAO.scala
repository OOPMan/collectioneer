package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.RelationshipCollection

import java.util.UUID

trait RelationshipCollectionDAO:
  def createRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Array[Int]
  def createOrUpdateRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Array[Int]
  def deleteRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Array[Int]
  def getAllMatchingRelationshipPKs(relationshipPKs: Seq[UUID]): List[RelationshipCollection]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): List[RelationshipCollection]
