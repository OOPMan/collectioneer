package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.h2
import com.oopman.collectioneer.db.traits.dao.raw.RelationshipDAO
import com.oopman.collectioneer.db.traits.entity.raw.Relationship
import scalikejdbc.DBSession

import java.util.UUID

object RelationshipDAO extends RelationshipDAO:
  def createRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] =
    h2.queries.raw.RelationshipQueries
      .insert
      .batch(h2.entity.raw.Relationship.relationshipSeqToBatchUpsertSeq(relationships): _*)
      .apply()

  def createOrUpdateRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] =
    h2.queries.raw.RelationshipQueries
      .upsert
      .batch(h2.entity.raw.Relationship.relationshipSeqToBatchUpsertSeq(relationships): _*)
      .apply()

  def deleteRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] = ???

  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): List[Relationship] = ???

  def getAllMatchingRelatedCollectionPKs(relatedCollectionPKs: Seq[UUID])(implicit session: DBSession): List[Relationship] = ???
  
