package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Relationship

import java.util.UUID

trait RelationshipDAO:
  def createRelationships(relationships: Seq[Relationship]): Array[Int]
  def createOrUpdateRelationships(relationships: Seq[Relationship]): Array[Int]
  def deleteRelationships(relationships: Seq[Relationship]): Array[Int]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): List[Relationship]
  def getAllMatchingRelatedCollectionPKs(relatedCollectionPKs: Seq[UUID]): List[Relationship]
  
