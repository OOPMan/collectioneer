package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.traits
import scalikejdbc._

object CollectionRelatedCollectionPropertyValueSetQueries extends traits.queries.raw.CollectionRelatedCollectionPropertyValueSetQueries:
  def insert =
    sql"""
       INSERT INTO COLLECTION__RELATED_COLLECTION__PROPERTY_VALUE_SET(collection__related_collection_pk, property_value_set_pk, index)  
       VALUES (?, ?, ?)
       """
    
  def upsert =
    sql"""
       MERGE INTO COLLECTION__RELATED_COLLECTION__PROPERTY_VALUE_SET(collection__related_collection_pk, property_value_set_pk, index)  
       KEY (collection__related_collection_pk, property_value_set_pk)
       VALUES (?, ?, ?)
       """
