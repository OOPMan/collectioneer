package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.db.SortDirection
import com.oopman.collectioneer.db.traits.entity.raw.Property
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

  def allMatchingConstraints(comparisonSQL: Option[String],
                             collectionPKs: Option[Seq[UUID]] = None,
                             parentCollectionPKs: Option[Seq[UUID]] = None,
                             sortProperties: Seq[(Property, SortDirection)] = Nil,
                             offset: Option[Int] = None,
                             limit: Option[Int] = None) =
    val comparisonSQLSyntax = comparisonSQL.map { comparisonSQL =>
      val fromSQLSyntax = SQLSyntax.createUnsafely(comparisonSQL)
      sqls"""
             WITH RECURSIVE
                    innerCTE1(root_collection_pk, collection_pk, level) AS (
                        SELECT pv.collection_pk AS root_collection_pk, pv.collection_pk, 0 AS level
                        FROM ($fromSQLSyntax) AS pv
                        UNION
                        SELECT
                            innerCTE1.root_collection_pk,
                            r.collection_pk,
                            innerCTE1.level + 1 as level
                        FROM innerCTE1
                        LEFT JOIN relationship AS r ON r.related_collection_pk = innerCTE1.collection_pk
                        WHERE r.relationship_type = 'SourceOfPropertiesAndPropertyValues'
                    )
                SELECT innerCTE1.root_collection_pk
                FROM innerCTE1
          """
    }
    val collectionPKsSQLSyntax = collectionPKs.map { collectionPKs =>
      sqls"""
             SELECT pk
             FROM collection
             WHERE pk IN ($collectionPKs)
          """
    }
    val parentCollectionPKsSQLSyntax = parentCollectionPKs.map { parentCollectionPKs =>
      if parentCollectionPKs.nonEmpty
      then
        sqls"""
               SELECT r.collection_pk
               FROM relationship AS r
               WHERE r.related_collection_pk IN ($parentCollectionPKs)
               AND r.relationship_type = 'ParentCollection'
            """
      else
        sqls"""
               SELECT c.pk
               FROM collection AS c
               LEFT JOIN relationship AS r ON r.related_collection_pk = c.pk
               WHERE r.relationship_type = 'ParentCollection'
               GROUP BY c.pk
            """
    }
    val cte1BodyComponents =
      comparisonSQLSyntax.map(Seq(_)).getOrElse(Nil) ++
      collectionPKsSQLSyntax.map(Seq(_)).getOrElse(Nil) ++
        parentCollectionPKsSQLSyntax.map(Seq(_)).getOrElse(Nil)
    val cte1BodySQL =
      if cte1BodyComponents.nonEmpty
      then
        sqls"""WHERE c.pk IN (${cte1BodyComponents.reduce((left, right) => sqls"$left INTERSECT $right")})"""
      else SQLSyntax.empty
    val unpackedSortProperties = for {
      (sortProperty, direction) <- sortProperties
      propertyType <- sortProperty.propertyTypes
    } yield (sortProperty.pk, propertyType, direction)
    val indexedSortProperties = unpackedSortProperties
      .zipWithIndex
      .map {
        case ((pk, propertyType, direction), index) => (
          SQLSyntax.createUnsafely(pk.toString),
          SQLSyntax.createUnsafely(propertyType.toString),
          if direction.eq(SortDirection.Asc) then SQLSyntax.asc else SQLSyntax.desc,
          SQLSyntax.createUnsafely(index.toString)
        )
      }
    val cte3OutputsSQL = indexedSortProperties
      .map((pk, propertyType, direction, propertyIndex) => sqls"property$propertyIndex")
      .reduceOption((left, right) => sqls"$left, $right")
      .map(s => sqls",$s")
      .getOrElse(SQLSyntax.empty)
    val cte3SelectsSQL = indexedSortProperties
      .map((pk, propertyType, direction, propertyIndex) => sqls"array_agg(property$propertyIndex.property_value)")
      .reduceOption((left, right) => sqls"$left, $right")
      .map(s => sqls",$s")
      .getOrElse(SQLSyntax.empty)
    val cte3JoinsSQL = indexedSortProperties
      .map((pk, propertyType, direction, propertyIndex) => {
        sqls"""
            LEFT JOIN property_value_$propertyType AS property$propertyIndex ON (
              property$propertyIndex.property_pk = '$pk'::uuid AND
              (property$propertyIndex.collection_pk = cte2.top_level_collection_pk OR property$propertyIndex.collection_pk = ANY (cte2.related_collection_pks))
            )
        """
      })
      .reduceOption((left, right) => sqls"$left\n$right")
      .getOrElse(SQLSyntax.empty)
    val orderBySQL = indexedSortProperties
      .map((pk, propertyType, direction, propertyIndex) => {
        sqls"property$propertyIndex $direction"
      })
      .reduceOption((left, right) => sqls"$left, $right")
      .map(s => sqls"ORDER BY $s")
      .getOrElse(SQLSyntax.empty)
    val offsetSQL = offset.map(SQLSyntax.offset).getOrElse(SQLSyntax.empty)
    val limitSQL = limit.map(SQLSyntax.limit).getOrElse(SQLSyntax.empty)
    sql"""
          WITH RECURSIVE
              cte1(top_level_collection_pk, collection_pk, related_collection_pk, index, level)
                  AS (SELECT c.pk AS top_level_collection_pk,
                             c.pk as collection_pk,
                             c.pk AS related_collection_pk,
                             0    as index,
                             0    as level
                      FROM collection AS c
                      /* START cte1BodySQL */
                      $cte1BodySQL
                      /* END cte1BodySQL */
              ),
              cte2(top_level_collection_pk, related_collection_pks) AS (
                  SELECT cte1.top_level_collection_pk, array_agg(cte1.related_collection_pk) as related_collection_pks
                  FROM cte1
                  GROUP BY cte1.top_level_collection_pk
              ),
              cte3(top_level_collection_pk $cte3OutputsSQL) AS (
                  SELECT cte2.top_level_collection_pk $cte3SelectsSQL
                  FROM cte2
                  /* START cte3JoinsSQL */
                  $cte3JoinsSQL
                  /* END cte3JoinsSQL */
                  GROUP BY cte2.top_level_collection_pk
              )
          SELECT c.*, cte3.*
          FROM cte3
          INNER JOIN collection AS c ON c.pk = cte3.top_level_collection_pk
          $orderBySQL
          $offsetSQL
          $limitSQL
       """

    
  def allMatchingPKs =
    sql"""
         SELECT ${Collection.c1.result.*}
         FROM ${Collection.as(Collection.c1)}
         WHERE ${Collection.c1.pk} = ANY (?::uuid[])
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
    allInnerJoining("cte1", "collection_pk", prefixSQL=prefixSQL)
