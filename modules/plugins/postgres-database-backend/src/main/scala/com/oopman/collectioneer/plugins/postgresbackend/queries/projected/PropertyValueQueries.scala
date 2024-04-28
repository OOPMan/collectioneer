package com.oopman.collectioneer.plugins.postgresbackend.queries.projected

import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport.propertyTypeToCast
import scalikejdbc.*

import java.util.UUID

object PropertyValueQueries:

  // TODO: Use more limited list of PropertyTypes?
  def generateCTE7SelectComponent(inputPropertyType: PropertyType): String =
    val selectElements = PropertyType.values.map(propertyType =>
      if (inputPropertyType == propertyType)
      then s"pv.property_value::${propertyTypeToCast(propertyType)} AS property_value_$propertyType"
      else s"NULL::${propertyTypeToCast(propertyType)} AS property_value_$propertyType"
    ).mkString(", ")
    s"""SELECT pv.pk, pv.collection_pk, pv.property_pk, pv.index, $selectElements
          FROM property_value_$inputPropertyType AS pv
          INNER JOIN cte3 ON pv.collection_pk = ANY (cte3.distinct_collection_pks)
          INNER JOIN cte5 ON pv.property_pk = ANY (cte5.distinct_property_pks)"""

  def generateCTE7QueryComponent() =

    val innerSQL = PropertyType.values.map(generateCTE7SelectComponent).mkString(" UNION ALL ")
    val columnItems = PropertyType.values.map(propertyType => s"property_value_$propertyType").mkString(", ")
    (
      SQLSyntax.createUnsafely(s"pk, collection_pk, property_pk, index, $columnItems"),
      SQLSyntax.createUnsafely(innerSQL)
    )

  def propertyValuesByCollectionPKs(collectionPKs: Seq[UUID], propertyPKs: Seq[UUID] = Nil, deleted: Seq[Boolean] = Seq(false)) =
    val propertyPKsConditionSQLSyntax = propertyPKs.length match
      case 0 => SQLSyntax.empty
      case _ => sqls"AND p.pk IN ($propertyPKs)"
    val (cte7ColumnsSQLSyntax, cte7InnerSQLSyntax) = generateCTE7QueryComponent()
    sql"""WITH RECURSIVE cte1(top_level_collection_pk, collection_pk, related_collection_pk, index) AS (
              SELECT c.pk AS top_level_collection_pk, r.collection_pk, r.related_collection_pk, r.index
              FROM collection AS c
              LEFT JOIN relationship AS r ON r.collection_pk = c.PK
              WHERE
                c.pk IN ($collectionPKs)
                AND (r.relationship_type = 'SourceOfPropertiesAndPropertyValues' OR r.relationship_type IS NULL)
              UNION
              SELECT cte1.top_level_collection_pk, r.collection_pk, r.related_collection_pk, r.index
              FROM cte1 LEFT JOIN relationship AS r ON cte1.related_collection_pk = r.collection_pk
              WHERE r.relationship_type = 'SourceOfPropertiesAndPropertyValues' OR r.relationship_type IS NULL
          ),
          cte2(distinct_collection_pk) AS (
              SELECT DISTINCT ON (distinct_collection_pk) coalesce(cte1.related_collection_pk, cte1.top_level_collection_pk) AS distinct_collection_pk
              FROM cte1
          ),
          cte3(distinct_collection_pks) AS (
              SELECT array_agg(cte2.distinct_collection_pk) AS distiction_collection_pks
              FROM cte2
          ),
          cte4(distinct_property_pk) AS (
              SELECT distinct on (pc.property_pk) pc.property_pk
              FROM property__collection AS pc
              INNER JOIN cte2 ON cte2.distinct_collection_pk = pc.collection_pk
          ),
          cte5(distinct_property_pks) AS (
            SELECT array_agg(cte4.distinct_property_pk) AS distinct_property_pks
            FROM cte4
          ),
          cte6(top_level_collection_pk, related_collection_pk, property_pk) AS (
            SELECT top_level_collection_pk,
                   related_collection_pk,
                   property_pk
            FROM cte1
            LEFT JOIN property__collection AS pc ON (cte1.top_level_collection_pk = pc.collection_pk OR pc.collection_pk = cte1.related_collection_pk)
            WHERE pc.property_pk IS NOT NULL
            GROUP BY top_level_collection_pk, related_collection_pk, property_pk
          ),
          cte7($cte7ColumnsSQLSyntax) AS ($cte7InnerSQLSyntax),
          cte8(top_level_collection_pk, related_collection_pk, property_pk, property_value_bigint, property_value_boolean, property_value_bytes, property_value_date,
               property_value_double, property_value_float, property_value_int, property_value_json, property_value_numeric,
               property_value_smallint, property_value_text, property_value_time, property_value_timestamp, property_value_uuid
              ) AS (
              SELECT
                  cte6.top_level_collection_pk,
                  cte6.related_collection_pk,
                  cte6.property_pk,
                  array_remove(array_agg(cte7.property_value_bigint ORDER BY cte7.index), NULL) AS property_value_bigint,
                  array_remove(array_agg(cte7.property_value_boolean ORDER BY cte7.index), NULL) AS property_value_boolean,
                  array_remove(array_agg(cte7.property_value_bytes ORDER BY cte7.index), NULL) AS property_value_bytes,
                  array_remove(array_agg(cte7.property_value_date ORDER BY cte7.index), NULL) AS property_value_date,
                  array_remove(array_agg(cte7.property_value_double ORDER BY cte7.index), NULL) AS property_value_double,
                  array_remove(array_agg(cte7.property_value_float ORDER BY cte7.index), NULL) AS property_value_float,
                  array_remove(array_agg(cte7.property_value_int ORDER BY cte7.index), NULL) AS property_value_int,
                  array_remove(array_agg(cte7.property_value_json ORDER BY cte7.index), NULL) AS property_value_json,
                  array_remove(array_agg(cte7.property_value_numeric ORDER BY cte7.index), NULL) AS property_value_numeric,
                  array_remove(array_agg(cte7.property_value_smallint ORDER BY cte7.index), NULL) AS property_value_smallint,
                  array_remove(array_agg(cte7.property_value_text ORDER BY cte7.index), NULL) AS property_value_text,
                  array_remove(array_agg(cte7.property_value_time ORDER BY cte7.index), NULL) AS property_value_time,
                  array_remove(array_agg(cte7.property_value_timestamp ORDER BY cte7.index), NULL) AS property_value_timestamp,
                  array_remove(array_agg(cte7.property_value_uuid ORDER BY cte7.index), NULL) AS property_value_uuid
              FROM cte6
              LEFT JOIN cte7 ON (
                  cte6.property_pk = cte7.property_pk AND
                  ((cte6.top_level_collection_pk = cte7.collection_pk AND cte6.related_collection_pk IS NULL) OR cte6.related_collection_pk = cte7.collection_pk))
              GROUP BY cte6.top_level_collection_pk, cte6.related_collection_pk, cte6.property_pk
          )
          SELECT
              p.*,
              cte8.property_value_bigint,
              cte8.property_value_boolean,
              cte8.property_value_bytes,
              cte8.property_value_date,
              cte8.property_value_double,
              cte8.property_value_float,
              cte8.property_value_int,
              cte8.property_value_json,
              cte8.property_value_numeric,
              cte8.property_value_smallint,
              cte8.property_value_text,
              cte8.property_value_time,
              cte8.property_value_timestamp,
              cte8.property_value_uuid,
              cte8.top_level_collection_pk,
              cte8.related_collection_pk,
              cte8.property_pk
          FROM cte8
          INNER JOIN property AS p ON cte8.property_pk = p.pk
          WHERE p.deleted IN ($deleted) $propertyPKsConditionSQLSyntax
        """