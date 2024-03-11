package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.traits
import scalikejdbc._

object PropertyPropertyValueSetQueries extends traits.queries.raw.PropertyPropertyValueSetQueries:
  def insert =
    sql"""
       INSERT INTO PROPERTY__PROPERTY_VALUE_SET(property_pk, property_value_set_pk, index, relationship)
       VALUES (?, ?, ?, ?)
       """

  def upsert =
    sql"""
       MERGE INTO PROPERTY__PROPERTY_VALUE_SET(property_pk, property_value_set_pk, index, relationship)
       KEY (property_pk, property_value_set_pk)
       VALUES (?, ?, ?, ?)
       """

