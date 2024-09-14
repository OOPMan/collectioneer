package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollectionRelationshipType
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
    
  def allPKs =
    sql"""
          SELECT p.pk
          FROM property AS p
       """

  def allMatchingPKs(propertyPKs: Seq[UUID]) =
    sql"""
          SELECT ${Property.p1.result.*}
          FROM ${Property.as(Property.p1)}
          WHERE ${Property.p1.pk} IN (${propertyPKs})
       """

  def innerJoiningPropertyCollection(fromSQL: String,
                                     fromColumn: String,
                                     propertyCollectionRelationshipType: PropertyCollectionRelationshipType,
                                     distinctOnColumnExpression: Option[String] = Some("p.pk"),
                                     prefixSQL: String = "",
                                     suffixSQL: String = "") =
    val distinctOnSQL = distinctOnColumnExpression
      .map(columnExpression => s"DISTINCT ON ($columnExpression)")
      .getOrElse("")
    SQL(s"""
            $prefixSQL
            SELECT $distinctOnSQL p.*
            FROM $fromSQL AS f1
            INNER JOIN property__collection AS pc ON pc.collection_pk = f1.$fromColumn
            INNER JOIN property AS p ON p.pk = pc.collection_pk
            WHERE pc.property__collection_relationship_type = '${propertyCollectionRelationshipType.toString}'
            $suffixSQL
        """)
