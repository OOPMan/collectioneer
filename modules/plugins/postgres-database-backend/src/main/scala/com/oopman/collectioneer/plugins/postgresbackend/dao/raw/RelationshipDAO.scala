package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.traits.dao.raw.RelationshipDAO
import com.oopman.collectioneer.db.traits.entity.raw.Relationship
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.DBSession

import java.util.UUID

object RelationshipDAO extends RelationshipDAO:
  def createRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] =
    postgresbackend.queries.raw.RelationshipQueries
      .insert
      .batch(postgresbackend.entity.raw.Relationship.relationshipSeqToBatchUpsertSeq(relationships): _*)
      .apply()

  def createOrUpdateRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] =
    postgresbackend.queries.raw.RelationshipQueries
      .upsert
      .batch(postgresbackend.entity.raw.Relationship.relationshipSeqToBatchUpsertSeq(relationships): _*)
      .apply()

  def deleteRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] = ???

  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): List[Relationship] = ???

  def getAllMatchingRelatedCollectionPKs(relatedCollectionPKs: Seq[UUID])(implicit session: DBSession): List[Relationship] = ???
  
