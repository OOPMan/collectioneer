package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType
import scalikejdbc.*

import java.util.UUID

object RelationshipQueries:
  def insert =
    sql"""
          INSERT INTO relationship(pk, collection_pk, related_collection_pk, relationship_type, index)
          VALUES (?, ?, ?, ?::relationship_type, ?)
       """
    
  def upsert =
    sql"""
          INSERT INTO relationship(pk, collection_pk, related_collection_pk, relationship_type, index)
          VALUES (?, ?, ?, ?::relationship_type, ?)
          ON CONFLICT (pk) DO UPDATE
          SET collection_pk = excluded.collection_pk, related_collection_pk = excluded.related_collection_pk,
          relationship_type = excluded.relationship_type::relationship_type, index = excluded.index, modified = now()
       """

  def collectionPKIn(collectionPKs: Seq[UUID]) =
    sqls"collection_pk IN ($collectionPKs)"

  def relatedCollectionPKIn(relatedCollectionPKs: Seq[UUID]) =
    sqls"related_collection_pk IN ($relatedCollectionPKs)"

  def relationshipTypeIn(relationshipTypes: Seq[RelationshipType]) =
    sqls"relationship_type IN ($relationshipTypes)"
    
  def selectBySQLSyntax(sqlSyntaxSeq: SQLSyntax*) =
    val whereClauses = sqlSyntaxSeq.reduce((lhs, rhs) => lhs.and(rhs))
    sql"""
          SELECT *
          FROM relationship
          WHERE $whereClauses
       """

  def selectByRelatedCollectionPKsAndRelationshipTypes(relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]) =
    selectBySQLSyntax(relatedCollectionPKIn(relatedCollectionPKs), relationshipTypeIn(relationshipTypes))

  def selectByCollectionPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]) =
    selectBySQLSyntax(collectionPKIn(collectionPKs), relationshipTypeIn(relationshipTypes))
    
  def selectByPKsAndRelationshipTypes(collectionPKs: Seq[UUID], relatedCollectionPKs: Seq[UUID], relationshipTypes: Seq[RelationshipType]) =
    selectBySQLSyntax(collectionPKIn(collectionPKs), relatedCollectionPKIn(relatedCollectionPKs), relationshipTypeIn(relationshipTypes))

