package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import scalikejdbc.*

object RelationshipCollectionQueries:
  def insert =
    sql"""
          INSERT INTO relationship_collection(relationship_pk, collection_pk, index)
          VALUES (?, ?, ?)
       """

  def upsert =
    sql"""
          INSERT INTO relationship_collection(relationship_pk, collection_pk, index)
          VALUES (?, ?, ?)
          ON CONFLICT(relationship_pk, collection_pk) DO UPDATE
          SET index = excluded.index, modified = now()
       """
