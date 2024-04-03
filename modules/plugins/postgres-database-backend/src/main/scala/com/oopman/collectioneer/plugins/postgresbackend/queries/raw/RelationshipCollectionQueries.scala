package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import scalikejdbc.*

object RelationshipCollectionQueries:
  def insert =
    sql"""
          INSERT INTO relationship__collection(relationship_pk, collection_pk, index)
          VALUES (?, ?, ?)
       """

  def upsert =
    sql"""
          MERGE INTO relationship__collection(relationship_pk, collection_pk, index, modified)
          KEY (relationship_pk, collection_pk)
          VALUES (?, ?, ?, now())
       """
