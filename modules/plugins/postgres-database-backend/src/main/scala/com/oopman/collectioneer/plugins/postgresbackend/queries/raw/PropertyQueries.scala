package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollectionRelationshipType
import com.oopman.collectioneer.plugins.postgresbackend.entity.raw.Property
import scalikejdbc.*

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

  def allMatchingPKs =
    sql"""
          SELECT ${Property.p1.result.*}
          FROM ${Property.as(Property.p1)}
          WHERE ${Property.p1.pk} = ANY (?::uuid[])
       """
    
  def allByPropertyCollection(selectColumnExpression: String = "p.*") =
    SQL(s"""
          SELECT $selectColumnExpression, array_agg(pc.collection_pk) AS collection_pks
          FROM property_collection AS pc
          INNER JOIN property AS p ON p.pk = pc.property_pk
          WHERE pc.collection_pk = ANY (?::uuid[])
          AND pc.property_collection_relationship_type = ANY (?::property_collection_relationship_type[])
          GROUP BY p.pk
       """)

  def innerJoiningPropertyCollection(fromSQL: String,
                                     fromColumn: String,
                                     propertyCollectionRelationshipType: PropertyCollectionRelationshipType,
                                     distinctOnColumnExpression: Option[String] = Some("p.pk"),
                                     selectColumnExpression: String = "p.*",
                                     prefixSQL: String = "",
                                     suffixSQL: String = "") =
    val distinctOnSQL = distinctOnColumnExpression
      .map(columnExpression => s"DISTINCT ON ($columnExpression)")
      .getOrElse("")
    SQL(s"""
            $prefixSQL
            SELECT $distinctOnSQL $selectColumnExpression
            FROM $fromSQL AS f1
            INNER JOIN property_collection AS pc ON pc.collection_pk = f1.$fromColumn
            INNER JOIN property AS p ON p.pk = pc.collection_pk
            WHERE pc.property_collection_relationship_type = '${propertyCollectionRelationshipType.toString}'
            $suffixSQL
        """)
