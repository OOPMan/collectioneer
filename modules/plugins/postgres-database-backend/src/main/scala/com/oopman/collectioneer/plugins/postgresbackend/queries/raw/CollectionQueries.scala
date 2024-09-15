package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.plugins.postgresbackend.entity.raw.Collection
import scalikejdbc.*

import java.util.UUID

object CollectionQueries:
  def insert =
    sql"""
          INSERT INTO collection(pk, virtual, deleted)
          VALUES ( ?, ?, ? );
       """

  def upsert =
    sql"""
          INSERT INTO collection(pk, virtual, deleted)
          VALUES (?, ?, ?)
          ON CONFLICT(pk) DO UPDATE
          set virtual = excluded.virtual, deleted = excluded.deleted, modified = now()
       """

  def all =
    sql"""
         SELECT ${Collection.c1.result.*}
         FROM ${Collection.as(Collection.c1)}
    """

  def allMatchingPKs(collectionPKs: Seq[UUID]) =
    sql"""
         SELECT ${Collection.c1.result.*}
         FROM ${Collection.as(Collection.c1)}
         WHERE ${Collection.c1.pk} IN (${collectionPKs})
       """

  def allInnerJoining(fromSQL: String, 
                      fromColumn: String,
                      distinctOnColumnExpression: Option[String] = Some("c1.pk"),
                      selectColumnExpression: String = "c1.*", 
                      prefixSQL: String = "", 
                      suffixSQL: String = "") =
    val distinctOnSQL = distinctOnColumnExpression
      .map(columnExpression => s"DISTINCT ON ($columnExpression)")
      .getOrElse("")
    SQL(s"""
          $prefixSQL
          SELECT $distinctOnSQL $selectColumnExpression
          FROM $fromSQL AS f1
          INNER JOIN collection AS c1 ON c1.pk = f1.$fromColumn
          $suffixSQL
        """)

  def allRelatedMatchingPropertyValues(propertyValuesFromSQL: String) =
    val prefixSQL =
      s"""
        WITH RECURSIVE cte1(root_collection_pk, collection_pk, level) AS (
            SELECT pv.collection_pk AS root_collection_pk, pv.collection_pk, 0 AS level
            FROM ($propertyValuesFromSQL) AS pv
            UNION
            SELECT
                cte1.root_collection_pk,
                r.collection_pk,
                cte1.level + 1 as level
            FROM cte1
            LEFT JOIN relationship AS r ON r.related_collection_pk = cte1.collection_pk
            WHERE r.relationship_type = 'SourceOfPropertiesAndPropertyValues'
        )
      """
    allInnerJoining("cte1", "collection_pk", prefixSQL)
