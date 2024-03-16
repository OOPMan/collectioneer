package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.traits.queries.raw.PropertyCollectionQueries
import scalikejdbc.*

object PropertyCollectionQueries extends PropertyCollectionQueries:
  def insert =
    sql"""
          INSERT INTO property__collection(property_pk, collection_pk, index, property_collection_relationship_type)
          VALUES (?, ?, ?, ?)         
       """
    
  def upsert =
    sql"""
          MERGE INTO property__collection(property_pk, collection_pk, index, property_collection_relationship_type, modified)
          KEY (property_pk, collection_pk)
          VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP())        
       """
  
