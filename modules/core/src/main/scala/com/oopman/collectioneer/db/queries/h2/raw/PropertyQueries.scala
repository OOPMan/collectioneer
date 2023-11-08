package com.oopman.collectioneer.db.queries.h2.raw

import com.oopman.collectioneer.db.entity.raw.{Property, p1}
import scalikejdbc.*

object PropertyQueries:

  def insert =
    sql"""
          INSERT INTO PROPERTY(pk, property_name, property_types, deleted)
          VALUES ( ?, ?, ?, ? );
    """

  def upsert =
    sql"""
          MERGE INTO PROPERTY(pk, property_name, property_types, deleted, modified)
          KEY (pk)
          VALUES ( ?, ?, ?, ?, ? );
    """

  def selectAll =
    sql"""
          SELECT ${p1.result.*}
          FROM ${Property.as(p1)}
     """
