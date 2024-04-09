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

  /**
   * JDBC Parameter notes
   * 1. Array of UUIDs
   * 2. Array of RelationshipTypes
   * @return
   */
  def selectByRelatedCollectionPKsAndRelationshipTypes =
    sql"""
          SELECT *
          FROM relationship
          WHERE related_collection_pk = ANY (?::uuid[])
          AND relationship_type = ANY (?::relationship_type[])
       """

  def selectByCollectionPKsAndRelationshipTypes =
    sql"""
          SELECT *
          FROM relationship
          WHERE collection_pk = ANY (?::uuid[])
          AND relationship_type = ANY (?::relationship_type[])
       """
