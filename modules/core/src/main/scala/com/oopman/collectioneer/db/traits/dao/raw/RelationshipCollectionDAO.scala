package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.RelationshipCollection

import java.util.UUID

trait RelationshipCollectionDAO:
  def createRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Seq[Int]
  def createOrUpdateRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Seq[Int]
  def deleteRelationshipCollections(relationshipCollections: Seq[RelationshipCollection]): Seq[Int]
  def getAllMatchingRelationshipPKs(relationshipPKs: Seq[UUID]): Seq[RelationshipCollection]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): Seq[RelationshipCollection]
