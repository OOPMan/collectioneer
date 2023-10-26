package com.oopman.collectioneer.db.queries.h2

import com.oopman.collectioneer.db.entity.{Property, p1}
import scalikejdbc._

object PropertyQueries:

  def insert =
    sql"""
          INSERT INTO PROPERTY(pk, property_name, property_types, deleted, created, modified)
          VALUES (?, ?, ?, ?, ?, ?);
    """

  def upsert =
    sql"""
          MERGE INTO PROPERTY(pk, property_name, property_types, deleted, created, modified)
          KEY (pk)
          VALUES (?, ?, ?, ?, ?, ?)
    """

  def selectAll =
    sql"""
          SELECT ${p1.result.*}
          FROM ${Property.as(p1)}
     """
