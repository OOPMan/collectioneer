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

  def allByRelatedPropertyCollection(selectColumnExpression: String = "p.*") =
    SQL(s"""WITH RECURSIVE
            cte1(top_level_collection_pk, collection_pk, related_collection_pk, index) AS (
                SELECT c.pk AS top_level_collection_pk, r.collection_pk, r.related_collection_pk, r.index
                FROM collection AS c
                LEFT JOIN relationship AS r ON r.collection_pk = c.PK
                WHERE c.pk = ANY (?::uuid[])
                AND (r.relationship_type = 'SourceOfPropertiesAndPropertyValues' OR r.relationship_type IS NULL)
                UNION
                SELECT cte1.top_level_collection_pk, r.collection_pk, r.related_collection_pk, r.index
                FROM cte1
                LEFT JOIN relationship AS r ON cte1.related_collection_pk = r.collection_pk
                WHERE r.relationship_type = 'SourceOfPropertiesAndPropertyValues'
                OR r.relationship_type IS NULL),
            cte2(top_level_collection_pk, is_inherited, property_pk) AS (
                SELECT
                    top_level_collection_pk,
                    CASE WHEN array_position(array_agg(related_collection_pk), NULL) IS NULL THEN true ELSE false END as is_inherited,
                    property_pk
                FROM cte1
                LEFT JOIN property_collection AS pc ON (
                    cte1.top_level_collection_pk = pc.collection_pk
                    OR pc.collection_pk = cte1.related_collection_pk)
                WHERE pc.property_pk IS NOT NULL
                AND pc.property_collection_relationship_type = ANY (?::property_collection_relationship_type[])
                GROUP BY top_level_collection_pk, property_pk)
            SELECT cte2.is_inherited, cte2.top_level_collection_pk, $selectColumnExpression
            FROM cte2
            INNER JOIN property AS p ON p.pk = cte2.property_pk
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
