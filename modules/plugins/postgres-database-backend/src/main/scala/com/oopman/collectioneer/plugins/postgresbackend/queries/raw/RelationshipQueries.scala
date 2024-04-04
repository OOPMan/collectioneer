package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import scalikejdbc.*

object RelationshipQueries:
  def insert =
    sql"""
          INSERT INTO relationship(pk, collection_pk, related_collection_pk, relationship_type, index)
          VALUES (?, ?, ?, cast(? AS relationship_type), ?)
       """
    
  def upsert =
    sql"""
          INSERT INTO relationship(pk, collection_pk, related_collection_pk, relationship_type, index)
          VALUES (?, ?, ?, cast(? AS relationship_type), ?)
          ON CONFLICT (pk) DO UPDATE
          SET collection_pk = excluded.collection_pk, related_collection_pk = excluded.related_collection_pk,
          relationship_type = cast(excluded.relationship_type AS relationship_type), index = excluded.index, modified = now()
       """
