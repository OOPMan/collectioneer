package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.db.traits.queries.raw.RelationshipQueries
import scalikejdbc.*

object RelationshipQueries extends RelationshipQueries:
  def insert =
    sql"""
          INSERT INTO relationship(pk, collection_pk, related_collection_pk, relationship_type, index)
          VALUES (?, ?, ?, ?, ?)
       """
    
  def upsert =
    sql"""
          MERGE INTO relationship(pk, collection_pk, related_collection_pk, relationship_type, index, modified)
          KEY (pk)
          VALUES (?, ?, ?, ?, ?, now())
       """
