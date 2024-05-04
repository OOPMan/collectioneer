package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw.ScalikeRelationshipDAO
import com.oopman.collectioneer.db.traits.entity.raw.{Relationship, RelationshipType}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.DBSession

import java.util.UUID

object RelationshipDAOImpl extends ScalikeRelationshipDAO:
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

  def getRelationshipsByCollectionPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType])(implicit session: DBSession): List[Relationship] =
    postgresbackend.queries.raw.RelationshipQueries
      .selectByCollectionPKsAndRelationshipTypes
      .bind(
        session.connection.createArrayOf("varchar", collectionPKs.toArray),
        session.connection.createArrayOf("varchar", relationshipTypes.toArray)
      )
      .map(postgresbackend.entity.raw.Relationship.apply)
      .list
      .apply()

  def getRelationshipsByRelatedCollectionPKsAndRelationshipTypes(relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType])(implicit session: DBSession): List[Relationship] =
    postgresbackend.queries.raw.RelationshipQueries
      .selectByRelatedCollectionPKsAndRelationshipTypes
      .bind(
        session.connection.createArrayOf("varchar", relatedCollectionPKs.toArray),
        session.connection.createArrayOf("varchar", relationshipTypes.toArray)
      )
      .map(postgresbackend.entity.raw.Relationship.apply)
      .list
      .apply()

  def getRelationshipsByPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType])(implicit session: DBSession): List[Relationship] =
    postgresbackend.queries.raw.RelationshipQueries
      .selectByPKsAndRelationshipTypes
      .bind(
        session.connection.createArrayOf("varchar", collectionPKs.toArray),
        session.connection.createArrayOf("varchar", relatedCollectionPKs.toArray),
        session.connection.createArrayOf("varchar", relationshipTypes.toArray)
      )
      .map(postgresbackend.entity.raw.Relationship.apply)
      .list
      .apply()

