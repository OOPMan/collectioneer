package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import scalikejdbc.*

object PropertyCollectionQueries:
  def insert =
    sql"""
          INSERT INTO property__collection(property_pk, collection_pk, index, property__collection_relationship_type)
          VALUES (?, ?, ?, cast(? AS property__collection__relationship_type))
       """
    
  def upsert =
    sql"""
          INSERT INTO property__collection(property_pk, collection_pk, index, property__collection_relationship_type)
          VALUES (?, ?, ?, cast(? AS property__collection__relationship_type))
          ON CONFLICT(property_pk, collection_pk) DO UPDATE
          SET index = excluded.index, property__collection_relationship_type = excluded.property__collection_relationship_type,
          modified = now()
       """
  
