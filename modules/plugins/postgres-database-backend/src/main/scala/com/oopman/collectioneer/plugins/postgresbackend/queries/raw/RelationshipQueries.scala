package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import scalikejdbc.*

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

  private val collectionPKEqualsAny = sqls"collection_pk = ANY (?::uuid[])"
  private val relatedCollectionPKEqualsAny = sqls"related_collection_pk = ANY (?::uuid[])"
  private val relationshipTypeEqualsAny = sqls"relationship_type = ANY (?::relationship_type[])"

  private def selectBySQLSyntax(sqlSyntaxSeq: SQLSyntax*) =
    val whereClauses = sqlSyntaxSeq.reduce((lhs, rhs) => lhs.and(rhs))
    sql"""
          SELECT *
          FROM relationship
          WHERE $whereClauses
       """

  def selectByRelatedCollectionPKsAndRelationshipTypes: SQL[Nothing, NoExtractor] =
    selectBySQLSyntax(relatedCollectionPKEqualsAny, relationshipTypeEqualsAny)

  def selectByCollectionPKsAndRelationshipTypes: SQL[Nothing, NoExtractor] =
    selectBySQLSyntax(collectionPKEqualsAny, relationshipTypeEqualsAny)

  def selectByPKsAndRelationshipTypes: SQL[Nothing, NoExtractor] =
    selectBySQLSyntax(collectionPKEqualsAny, relatedCollectionPKEqualsAny, relationshipTypeEqualsAny)

