package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.RelationshipCollection
import scalikejdbc.DBSession

import java.util.UUID

trait RelationshipCollectionDAO:
  def createRelationshipCollections(relationshipCollections: Seq[RelationshipCollection])(implicit session: DBSession): Array[Int]
  def createOrUpdateRelationshipCollections(relationshipCollections: Seq[RelationshipCollection])(implicit session: DBSession): Array[Int]
  def deleteRelationshipCollections(relationshipCollections: Seq[RelationshipCollection])(implicit session: DBSession): Array[Int]
  def getAllMatchingRelationshipPKs(relationshipPKs: Seq[UUID])(implicit session: DBSession): List[RelationshipCollection]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): List[RelationshipCollection]
