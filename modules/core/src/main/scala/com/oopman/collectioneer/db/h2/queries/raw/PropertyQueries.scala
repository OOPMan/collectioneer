package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.entity.raw.Property
import com.oopman.collectioneer.db.traits.queries.raw.PropertyQueries
import scalikejdbc.*

object PropertyQueries extends PropertyQueries:

  def insert =
    sql"""
          INSERT INTO property(pk, property_name, property_types, deleted)
          VALUES ( ?, ?, ?, ? );
    """

  def upsert =
    sql"""
          MERGE INTO property(pk, property_name, property_types, deleted, modified)
          KEY (pk)
          VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP());
    """

  def all =
    sql"""
          SELECT ${Property.p1.result.*}
          FROM ${Property.as(Property.p1)}
     """
