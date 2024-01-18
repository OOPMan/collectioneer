package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.entity.raw.Property
import com.oopman.collectioneer.db.traits.queries.raw.PropertyQueries
import scalikejdbc.*

object PropertyQueries extends PropertyQueries:

  val insert =
    sql"""
          INSERT INTO PROPERTY(pk, property_name, property_types, deleted)
          VALUES ( ?, ?, ?, ? );
    """

  val upsert =
    sql"""
          MERGE INTO PROPERTY(pk, property_name, property_types, deleted, modified)
          KEY (pk)
          VALUES ( ?, ?, ?, ?, ? );
    """

  val all =
    sql"""
          SELECT ${Property.p1.result.*}
          FROM ${Property.as(Property.p1)}
     """
