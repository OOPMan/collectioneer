package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import scalikejdbc.*

object RelationshipQueries:
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
