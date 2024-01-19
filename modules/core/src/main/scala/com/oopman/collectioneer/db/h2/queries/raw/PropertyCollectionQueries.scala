package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.traits.queries.raw.PropertyCollectionQueries
import scalikejdbc.*

object PropertyCollectionQueries extends PropertyCollectionQueries:
  def insert =
    sql"""
          INSERT INTO PROPERTY__COLLECTION(PROPERTY_PK, COLLECTION_PK, PROPERTY_VALUE_SET_PK, INDEX, RELATIONSHIP)
          VALUES ( ?, ?, ?, ?, ? );
       """

  def upsert =
    sql"""
          MERGE INTO PROPERTY__COLLECTION(PROPERTY_PK, COLLECTION_PK, PROPERTY_VALUE_SET_PK, INDEX, RELATIONSHIP, MODIFIED)
          KEY (PROPERTY_PK, COLLECTION_PK)
          VALUES ( ?, ?, ?, ?, ?, ? );
       """