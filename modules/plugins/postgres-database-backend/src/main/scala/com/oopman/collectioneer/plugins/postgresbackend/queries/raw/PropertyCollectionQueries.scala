package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import scalikejdbc.*

object PropertyCollectionQueries:
  def insert =
    sql"""
          INSERT INTO property__collection(property_pk, collection_pk, index, property_collection_relationship_type)
          VALUES (?, ?, ?, ?)         
       """
    
  def upsert =
    sql"""
          INSERT INTO property__collection(property_pk, collection_pk, index, property_collection_relationship_type)
          VALUES (?, ?, ?, ?)
          ON CONFLICT(property_pk, collection_pk) DO UPDATE
          SET index = excluded.index, property_collection_relationship_type = excluded.property_collection_relationship_type,
          modified = now()
       """
  
