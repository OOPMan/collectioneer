package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, PropertyType}
import scalikejdbc.*
import com.oopman.collectioneer.db.{entity, scalikejdbc, traits}
import com.oopman.collectioneer.plugins.postgresbackend

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID


object CollectionDAOImpl extends scalikejdbc.traits.dao.raw.ScalikeCollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.CollectionQueries
      .insert
      .batch(postgresbackend.entity.raw.Collection.collectionsSeqToBatchInsertSeq(collections): _*)
      .apply()

  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.CollectionQueries
      .upsert
      .batch(postgresbackend.entity.raw.Collection.collectionsSeqToBatchInsertSeq(collections): _*)
      .apply()

  def getAll(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    postgresbackend.queries.raw.CollectionQueries
      .all
      .map(postgresbackend.entity.raw.Collection(postgresbackend.entity.raw.Collection.c1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    postgresbackend.queries.raw.CollectionQueries
      .allMatchingPKs(collectionPKs)
      .map(postgresbackend.entity.raw.Collection(postgresbackend.entity.raw.Collection.c1.resultName))
      .list
      .apply()

  def getAllMatchingPropertyValues(comparisons: Comparison*)(implicit session: DBSession = AutoSession): List[Collection] =
    // TODO: Extract this code for common usage
    val comparison = comparisons.reduce((c1, c2) => c1 and c2)
    def makeJDBCArray(values: Array[Value]) =
      val connection = session.connection
      values match
        case v: Array[BigInt] => connection.createArrayOf("BIGINT", values.toArray)
        case v: Array[Boolean] => connection.createArrayOf("BOOLEAN", values.toArray)
        case v: Array[Byte] => connection.createArrayOf("TINYINT", values.toArray)
        case v: Array[LocalDate] => connection.createArrayOf("DATE", values.toArray)
        case v: Array[Double] => connection.createArrayOf("DOUBLE", values.toArray)
        case v: Array[Float] => connection.createArrayOf("FLOAT", values.toArray)
        case v: Array[Int] => connection.createArrayOf("INT", values.toArray)
        case v: Array[io.circe.Json] => connection.createArrayOf("VARCHAR", v.map(_.spaces2).toArray)
        case v: Array[BigDecimal] => connection.createArrayOf("NUMERIC", values.toArray)
        case v: Array[String] => connection.createArrayOf("VARCHAR", values.toArray)
        case v: Array[OffsetTime] => connection.createArrayOf("TIME", values.toArray)
        case v: Array[ZonedDateTime] => connection.createArrayOf("TIMESTAMP", values.toArray)
        case v: Array[UUID] => connection.createArrayOf("VARCHAR", values.toArray)

    def propertyTypeToCast(p: PropertyType) = p match
      case PropertyType.bytes => "bytea"
      case PropertyType.float => "real"
      case PropertyType.double => "double precision"
      case PropertyType.json => "jsonb"
      case _ => p.toString

    def comparisonToSQL(comparison: Comparison): (String, Seq[Any]) =
      comparison match
        case PropertyValueComparison(lhs, operator, rhs) =>
          val tableNames = lhs.propertyTypes.map(propertyType => s"property_value_${propertyType.toString}")
          val operatorSymbol = operator match {
            case Operator.greaterThan =>  " > "
            case Operator.greaterThanOrEqualTo => " >= "
            case Operator.equalTo => " = "
            case Operator.notEqualTo => " != "
            case Operator.lessThanOrEualTo => " <= "
            case Operator.lessThan => " < "
            case Operator.like => " LIKE "
          }
          val (suffix, value, castSuffix) = rhs match {
            case a: Seq[Value] => (" ANY ", makeJDBCArray(a.toArray), "[]")
            case _ => ("", rhs, "")
          }
          val x = lhs.propertyTypes.map(propertyType =>
            val tableName = s"property_value_${propertyType.toString}"
            val cast = propertyTypeToCast(propertyType)
            (
              s"""
               | SELECT pv.collection_pk
               | FROM $tableName AS pv
               | WHERE pv.property_pk = ?::uuid
               | AND pv.property_value ${operatorSymbol}${suffix} (?::${cast}${castSuffix})
               |""".stripMargin,
              Seq(lhs.pk, value)
            )
          )
          val y = x.reduce((a, b) => (s"${a._1} UNION ${b._1}", a._2 ++ b._2))
          y
        case PropertyPropertyComparison(lhs, operator, rhs) => ("", Nil) // TODO: Implement
        case NestedComparison(lhs, operator, rhs) =>
          val (lhsSQL, lhsParameters) = comparisonToSQL(lhs)
          val (rhsSQL, rhsParameters) = comparisonToSQL(rhs)
          operator match
            case LogicalOperator.and => (s"($lhsSQL) INTERSECT ($rhsSQL)", lhsParameters ++ rhsParameters)
            case LogicalOperator.or => (s"($lhsSQL) UNION ($rhsSQL)", lhsParameters ++ rhsParameters)
    val (comparisonSQL, parameters) = comparisonToSQL(comparison)
    SQL(
      s"""
        | SELECT c2.*
        | FROM ($comparisonSQL) AS c1
        | INNER JOIN collection AS c2 ON c2.pk = c1.collection_pk
        |""".stripMargin)
      .bind(parameters: _*)
      .map(resultSet => postgresbackend.entity.raw.Collection.apply(resultSet))
      .list
      .apply()
