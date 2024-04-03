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
          MERGE INTO property__collection(property_pk, collection_pk, index, property_collection_relationship_type, modified)
          KEY (property_pk, collection_pk)
          VALUES (?, ?, ?, ?, now())
       """
  
