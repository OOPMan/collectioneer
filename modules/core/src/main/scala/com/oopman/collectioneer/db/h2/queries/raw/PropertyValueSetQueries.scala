package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.traits
import scalikejdbc._

object PropertyValueSetQueries extends traits.queries.raw.PropertyValueSetQueries:
  def insert =
    sql"""
          INSERT INTO PROPERTY_VALUE_SET (PK)
          VALUES (?)
       """

  def upsert =
    sql"""
          MERGE INTO PROPERTY_VALUE_SET (PK)
          KEY (PK)
          VALUES (?)
       """
