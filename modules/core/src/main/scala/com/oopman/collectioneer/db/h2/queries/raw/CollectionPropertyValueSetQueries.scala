package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.traits
import scalikejdbc._

object CollectionPropertyValueSetQueries extends traits.queries.raw.CollectionPropertyValueSetQueries:
  def insert =
    sql"""
       INSERT INTO COLLECTION__PROPERTY_VALUE_SET(collection_pk, property_value_set_pk, index)        
       VALUES (?, ?, ?)
       """

  def upsert =
    sql"""
       MERGE INTO COLLECTION__PROPERTY_VALUE_SET(collection_pk, property_value_set_pk, index)  
       KEY (collection_pk, property_value_set_pk)
       VALUES (?, ?, ?)
       """
