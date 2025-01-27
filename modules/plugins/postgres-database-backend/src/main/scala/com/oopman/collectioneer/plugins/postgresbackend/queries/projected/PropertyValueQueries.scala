package com.oopman.collectioneer.plugins.postgresbackend.queries.projected

import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport.propertyTypeToCast
import scalikejdbc.*

object PropertyValueQueries:

  // TODO: Use more limited list of PropertyTypes?
  def generatePropertyValuesByCollectionPKsCTE7SelectComponent(inputPropertyType: PropertyType): String =
    val selectElements = PropertyType.values.map(propertyType =>
      if (inputPropertyType == propertyType)
      then s"pv.property_value::${propertyTypeToCast(propertyType)} AS property_value_$propertyType"
      else s"NULL::${propertyTypeToCast(propertyType)} AS property_value_$propertyType"
    ).mkString(", ")
    s"""SELECT pv.pk, pv.collection_pk, pv.property_pk, pv.index, $selectElements
        |FROM property_value_$inputPropertyType AS pv
        |INNER JOIN cte3 ON pv.collection_pk = ANY (cte3.distinct_collection_pks)
        |INNER JOIN cte5 ON pv.property_pk = ANY (cte5.distinct_property_pks)""".stripMargin

  def generatePropertyValuesByCollectionPKsCTE7QueryComponent() =

    val innerSQL = PropertyType.values.map(generatePropertyValuesByCollectionPKsCTE7SelectComponent).mkString(" UNION ALL ")
    val columnItems = PropertyType.values.map(propertyType => s"property_value_$propertyType").mkString(", ")
    (
      SQLSyntax.createUnsafely(s"pk, collection_pk, property_pk, index, $columnItems"),
      SQLSyntax.createUnsafely(innerSQL)
    )

  def propertyValuesByCollectionPKs(includePropertiesFilter: Boolean = false) =
    val cte8WhereClause = if includePropertiesFilter then sqls"WHERE cte6.property_pk = ANY (?::uuid[])" else SQLSyntax.empty
    val (cte7ColumnsSQLSyntax, cte7InnerSQLSyntax) = generatePropertyValuesByCollectionPKsCTE7QueryComponent()
    // TODO: Include complete details on top-level collection
    sql"""WITH RECURSIVE cte1(top_level_collection_pk, collection_pk, related_collection_pk, index, level) AS (
              SELECT c.pk AS top_level_collection_pk, c.pk as collection_pk, c.pk AS related_collection_pk, 0 as index, 0 as level
              FROM collection AS c
              WHERE c.pk = ANY (?::uuid[])
              UNION
              SELECT cte1.top_level_collection_pk, r.collection_pk, r.related_collection_pk, r.index, cte1.level + 1
              FROM cte1
              INNER JOIN relationship AS r ON cte1.related_collection_pk = r.collection_pk
              WHERE r.relationship_type = 'SourceOfPropertiesAndPropertyValues'
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
              FROM property_collection AS pc
              INNER JOIN cte2 ON cte2.distinct_collection_pk = pc.collection_pk
          ),
          cte5(distinct_property_pks) AS (
            SELECT array_agg(cte4.distinct_property_pk) AS distinct_property_pks
            FROM cte4
          ),
          cte6(top_level_collection_pk, related_collection_pk, property_pk) AS (
            SELECT top_level_collection_pk,
                   array_remove(array_agg(related_collection_pk), NULL) as related_collection_pk,
                   property_pk
            FROM cte1
            LEFT JOIN property_collection AS pc ON (cte1.top_level_collection_pk = pc.collection_pk OR pc.collection_pk = cte1.related_collection_pk)
            WHERE pc.property_pk IS NOT NULL
            GROUP BY top_level_collection_pk, property_pk
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
                  array_remove(array_agg(cte7.property_value_bigint     ORDER BY cte7.index), NULL) AS property_value_bigint,
                  array_remove(array_agg(cte7.property_value_boolean    ORDER BY cte7.index), NULL) AS property_value_boolean,
                  array_remove(array_agg(cte7.property_value_bytes      ORDER BY cte7.index), NULL) AS property_value_bytes,
                  array_remove(array_agg(cte7.property_value_date       ORDER BY cte7.index), NULL) AS property_value_date,
                  array_remove(array_agg(cte7.property_value_double     ORDER BY cte7.index), NULL) AS property_value_double,
                  array_remove(array_agg(cte7.property_value_float      ORDER BY cte7.index), NULL) AS property_value_float,
                  array_remove(array_agg(cte7.property_value_int        ORDER BY cte7.index), NULL) AS property_value_int,
                  array_remove(array_agg(cte7.property_value_json       ORDER BY cte7.index), NULL) AS property_value_json,
                  array_remove(array_agg(cte7.property_value_numeric    ORDER BY cte7.index), NULL) AS property_value_numeric,
                  array_remove(array_agg(cte7.property_value_smallint   ORDER BY cte7.index), NULL) AS property_value_smallint,
                  array_remove(array_agg(cte7.property_value_text       ORDER BY cte7.index), NULL) AS property_value_text,
                  array_remove(array_agg(cte7.property_value_time       ORDER BY cte7.index), NULL) AS property_value_time,
                  array_remove(array_agg(cte7.property_value_timestamp  ORDER BY cte7.index), NULL) AS property_value_timestamp,
                  array_remove(array_agg(cte7.property_value_uuid       ORDER BY cte7.index), NULL) AS property_value_uuid
              FROM cte6
              LEFT JOIN cte7 ON (
                  cte6.property_pk = cte7.property_pk AND
                  ((cte6.top_level_collection_pk = cte7.collection_pk AND cte6.related_collection_pk = '{}') OR cte7.collection_pk = ANY (cte6.related_collection_pk)))
              $cte8WhereClause
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
              cte8.related_collection_pk
          FROM cte8
          INNER JOIN property AS p ON cte8.property_pk = p.pk
        """

  def generatePropertyValuesByParentPropertyPKsCTE3SelectComponent(inputPropertyType: PropertyType): String =
    val selectElements = PropertyType.values.map(propertyType =>
      if (inputPropertyType == propertyType)
      then s"pv.property_value::${propertyTypeToCast(propertyType)} AS property_value_$propertyType"
      else s"NULL::${propertyTypeToCast(propertyType)} AS property_value_$propertyType"
    ).mkString(", ")
    s"""SELECT pv.pk, pv.collection_pk, pv.property_pk, pv.index, $selectElements
        |FROM property_value_$inputPropertyType AS pv
        |INNER JOIN cte2 ON (cte2.property_pk = pv.property_pk AND cte2.collection_pk = pv.collection_pk)""".stripMargin

  def generatePropertyValuesByParentPropertyPKsCTE3QueryComponent() =
    val innerSQL = PropertyType.values.map(generatePropertyValuesByParentPropertyPKsCTE3SelectComponent).mkString(" UNION ALL ")
    val columnItems = PropertyType.values.map(propertyType => s"property_value_$propertyType").mkString(", ")
    (
      SQLSyntax.createUnsafely(s"pk, collection_pk, property_pk, index, $columnItems"),
      SQLSyntax.createUnsafely(innerSQL)
    )
    
  def propertyValuesByParentPropertyPKs(includeParentProperty: Boolean = true, includeChildProperty: Boolean = true) =
//    val (a, b) = if includeParentProperty
//      then
    val (cte3ColumnsSQLSyntax, cte3InnerSQLSyntax) = generatePropertyValuesByParentPropertyPKsCTE3QueryComponent()
    sql"""WITH
          cte1(collection_pk) AS (
              SELECT pc.collection_pk
              FROM property_collection AS pc
              WHERE pc.property_pk = ANY (?::uuid[])
              AND pc.property_collection_relationship_type = 'CollectionOfPropertiesOfProperty'),
          cte2(property_pk, collection_pk) AS (
              SELECT pc.property_pk, pc.collection_pk
              FROM property_collection AS pc
              INNER JOIN cte1 ON cte1.collection_pk = pc.collection_pk
              WHERE pc.property_collection_relationship_type = 'PropertyOfCollection'
          ),
          cte3($cte3ColumnsSQLSyntax) AS ($cte3InnerSQLSyntax),
          cte4(collection_pk, property_pk, property_value_bigint, property_value_boolean, property_value_bytes,
               property_value_date, property_value_double, property_value_float, property_value_int, property_value_json,
               property_value_numeric, property_value_smallint, property_value_text, property_value_time,
               property_value_timestamp, property_value_uuid) AS (
              SELECT cte3.collection_pk,
                     cte3.property_pk,
                     array_remove(array_agg(cte3.property_value_bigint      ORDER BY cte3.index), NULL) AS property_value_bigint,
                     array_remove(array_agg(cte3.property_value_boolean     ORDER BY cte3.index), NULL) AS property_value_boolean,
                     array_remove(array_agg(cte3.property_value_bytes       ORDER BY cte3.index), NULL) AS property_value_bytes,
                     array_remove(array_agg(cte3.property_value_date        ORDER BY cte3.index), NULL) AS property_value_date,
                     array_remove(array_agg(cte3.property_value_double      ORDER BY cte3.index), NULL) AS property_value_double,
                     array_remove(array_agg(cte3.property_value_float       ORDER BY cte3.index), NULL) AS property_value_float,
                     array_remove(array_agg(cte3.property_value_int         ORDER BY cte3.index), NULL) AS property_value_int,
                     array_remove(array_agg(cte3.property_value_json        ORDER BY cte3.index), NULL) AS property_value_json,
                     array_remove(array_agg(cte3.property_value_numeric     ORDER BY cte3.index), NULL) AS property_value_numeric,
                     array_remove(array_agg(cte3.property_value_smallint    ORDER BY cte3.index), NULL) AS property_value_smallint,
                     array_remove(array_agg(cte3.property_value_text        ORDER BY cte3.index), NULL) AS property_value_text,
                     array_remove(array_agg(cte3.property_value_time        ORDER BY cte3.index), NULL) AS property_value_time,
                     array_remove(array_agg(cte3.property_value_timestamp   ORDER BY cte3.index), NULL) AS property_value_timestamp,
                     array_remove(array_agg(cte3.property_value_uuid        ORDER BY cte3.index), NULL) AS property_value_uuid
              FROM cte3
              GROUP BY cte3.collection_pk, cte3.property_pk
          )
          SELECT
              cte4.collection_pk AS parent_property_pk,
              /*
              parent_property.property_name AS parent_property_name,
              parent_property.property_types AS parent_property_types,
              parent_property.deleted AS parent_property_deleted,
              parent_property.created AS parent_property_created,
              parent_property.modified AS parent_property_modified,
              */
              cte4.property_pk AS child_property_pk,
              /*
              child_property.property_name AS child_property_name,
              child_property.property_types AS child_property_types,
              child_property.deleted AS child_property_deleted,
              child_property.created AS child_property_created,
              child_property.modified AS child_property_modified,
              */
              property_value_bigint,
              property_value_boolean,
              property_value_bytes,
              property_value_date,
              property_value_double,
              property_value_float,
              property_value_int,
              property_value_json,
              property_value_numeric,
              property_value_smallint,
              property_value_text,
              property_value_time,
              property_value_timestamp,
              property_value_uuid
          FROM cte4
          /*INNER JOIN property AS parent_property ON parent_property.pk = cte4.collection_pk*/
          /*INNER JOIN property AS child_property ON child_property.pk = cte4.property_pk*/
          ;
       """