package com.oopman.collectioneer.db.h2.queries.raw

import scalikejdbc._

object PropertyValueSetQueries:
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
