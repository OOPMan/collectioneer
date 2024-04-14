package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.plugins.postgresbackend.entity.raw.Property
import scalikejdbc.*

import java.util.UUID

object PropertyQueries:

  def insert =
    sql"""
          INSERT INTO property(pk, property_name, property_types, deleted)
          VALUES (?, ?, cast(? AS property_type[]), ? );
    """

  def upsert =
    sql"""
          INSERT INTO property(pk, property_name, property_types, deleted)
          VALUES (?, ?, cast(? AS property_type[]), ?)
          ON CONFLICT(pk) DO UPDATE
          SET property_name = excluded.property_name, property_types = excluded.property_types,
          deleted = excluded.deleted, modified = now()
    """

  def all =
    sql"""
          SELECT ${Property.p1.result.*}
          FROM ${Property.as(Property.p1)}
     """

  def allMatchingPKs(propertyPKs: Seq[UUID]) =
    sql"""
          SELECT ${Property.p1.result.*}
          FROM ${Property.as(Property.p1)}
          WHERE ${Property.p1.pk} IN (${propertyPKs})
       """
