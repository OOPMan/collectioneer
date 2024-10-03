package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import scalikejdbc.*

import java.sql
import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID

object PropertyValueQueryDSLSupport:
  def makeJDBCArray(values: Array[Value])(implicit session: DBSession = AutoSession): sql.Array =
    val connection = session.connection
    val result = for (v <- values.headOption) yield v match {
      case v: BigInt => connection.createArrayOf("BIGINT", values.asInstanceOf[Array[Object]])
      case v: Boolean => connection.createArrayOf("BOOLEAN", values.asInstanceOf[Array[Object]])
      case v: Byte => connection.createArrayOf("BYTEA", values.asInstanceOf[Array[Object]])
      case v: LocalDate => connection.createArrayOf("DATE", values.asInstanceOf[Array[Object]])
      case v: Double => connection.createArrayOf("DOUBLE", values.asInstanceOf[Array[Object]])
      case v: Float => connection.createArrayOf("FLOAT", values.asInstanceOf[Array[Object]])
      case v: Int => connection.createArrayOf("INT", values.asInstanceOf[Array[Object]])
      case v: io.circe.Json =>
        val jsonStrings = values.map { case v: io.circe.Json => v.spaces2 }
        connection.createArrayOf("VARCHAR", jsonStrings.asInstanceOf[Array[Object]])
      case v: BigDecimal => connection.createArrayOf("NUMERIC", values.asInstanceOf[Array[Object]])
      case v: String => connection.createArrayOf("VARCHAR", values.asInstanceOf[Array[Object]])
      case v: OffsetTime => connection.createArrayOf("TIME", values.asInstanceOf[Array[Object]])
      case v: ZonedDateTime => connection.createArrayOf("TIMESTAMP", values.asInstanceOf[Array[Object]])
      case v: UUID => connection.createArrayOf("VARCHAR", values.asInstanceOf[Array[Object]])
      case _ => connection.createArrayOf("VARCHAR", Array.empty)
    }
    result.getOrElse(connection.createArrayOf("VARCHAR", Array.empty))

  def propertyTypeToCast(propertyType: PropertyType): String = propertyType match
    case PropertyType.bytes => "bytea"
    case PropertyType.float => "real"
    case PropertyType.double => "double precision"
    case PropertyType.json => "jsonb"
    case _ => propertyType.toString

  def comparisonToSQL(comparison: Comparison)(implicit session: DBSession = AutoSession): (String, Seq[Any]) = comparison match
    case PropertyValueComparison(lhs, operator, rhs) =>
      val tableNames = lhs.propertyTypes.map(propertyType => s"property_value_${propertyType.toString}")
      val operatorSymbol = operator match {
        case Operator.greaterThan => " > "
        case Operator.greaterThanOrEqualTo => " >= "
        case Operator.equalTo => " = "
        case Operator.notEqualTo => " != "
        case Operator.lessThanOrEualTo => " <= "
        case Operator.lessThan => " < "
        case Operator.like => " LIKE "
      }
      val (operatorSuffix, value, castSuffix) = rhs match {
        case seq: Seq[Value] => (" ANY ", makeJDBCArray(seq.toArray), "[]")
        case arr: Array[Value] => (" ANY ", makeJDBCArray(arr), "[]")
        case _ => ("", rhs, "")
      }
      val sqlAndParameters = lhs.propertyTypes.map(propertyType =>
        val tableName = s"property_value_${propertyType.toString}"
        val cast = propertyTypeToCast(propertyType)
        (
          s"""
            SELECT pv.collection_pk
            FROM $tableName AS pv
            WHERE pv.property_pk = ?::uuid
            AND pv.property_value ${operatorSymbol}${operatorSuffix} (?::${cast}${castSuffix})
           """,
          Seq(lhs.pk, value)
        )
      )
      sqlAndParameters.reduce {
        case ((sqlA, parametersA), (sqlB, parametersB)) => (s"${sqlA} UNION ${sqlB}", parametersA ++ parametersB)
      }

    case PropertyPropertyComparison(lhs, operator, rhs) =>
      // TODO: Implement
      ("", Nil)

    case NestedComparison(lhs, operator, rhs) =>
      val (lhsSQL, lhsParameters) = comparisonToSQL(lhs)
      val (rhsSQL, rhsParameters) = comparisonToSQL(rhs)
      operator match
        case LogicalOperator.and => (s"($lhsSQL) INTERSECT ($rhsSQL)", lhsParameters ++ rhsParameters)
        case LogicalOperator.or => (s"($lhsSQL) UNION ($rhsSQL)", lhsParameters ++ rhsParameters)
  
  def comparisonsToSQL(comparisons: Seq[Comparison])(implicit sessions: DBSession = AutoSession): (String, Seq[Any]) =
    comparisonToSQL(comparisons.reduce((c1, c2) => c1 and c2))
    