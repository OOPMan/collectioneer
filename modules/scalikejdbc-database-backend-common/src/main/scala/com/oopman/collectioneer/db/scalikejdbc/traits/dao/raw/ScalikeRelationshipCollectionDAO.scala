package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.RelationshipCollection
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikeRelationshipCollectionDAO:
  def createRelationshipCollections(relationshipCollections: Seq[RelationshipCollection])(implicit session: DBSession): Seq[Int]
  def createOrUpdateRelationshipCollections(relationshipCollections: Seq[RelationshipCollection])(implicit session: DBSession): Seq[Int]
  def deleteRelationshipCollections(relationshipCollections: Seq[RelationshipCollection])(implicit session: DBSession): Seq[Int]
  def getAllMatchingRelationshipPKs(relationshipPKs: Seq[UUID])(implicit session: DBSession): Seq[RelationshipCollection]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): Seq[RelationshipCollection]
