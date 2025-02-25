package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import org.apache.commons.codec.binary.Hex
import scalikejdbc.*
import shapeless3.typeable.{TypeCase, Typeable}

import java.sql
import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID


object PropertyValueQueryDSLSupport:
  given byteArrayTypeable: Typeable[Array[Byte]] = new Typeable[Array[Byte]] {
    def describe = "Array[Byte]"

    override def castable(t: Any): Boolean = t match {
      case t: Array[Byte] => true
      case _ => false
    }
  }

  private val `Seq[BigInt]` = TypeCase[Seq[BigInt]]
  private val `Seq[Boolean]` = TypeCase[Seq[Boolean]]
  private val `Seq[Array[Byte]]` = TypeCase[Seq[Array[Byte]]]
  private val `Seq[LocalDate]` = TypeCase[Seq[LocalDate]]
  private val `Seq[Double]` = TypeCase[Seq[Double]]
  private val `Seq[Float]` = TypeCase[Seq[Float]]
  private val `Seq[Int]` = TypeCase[Seq[Int]]
  private val `Seq[Short]` = TypeCase[Seq[Short]]
  private val `Seq[io.circe.Json]` = TypeCase[Seq[io.circe.Json]]
  private val `Seq[BigDecimal]` = TypeCase[Seq[BigDecimal]]
  private val `Seq[LocalTime]` = TypeCase[Seq[LocalTime]]
  private val `Seq[ZonedDateTime]` = TypeCase[Seq[ZonedDateTime]]
  private val `Seq[UUID]` = TypeCase[Seq[UUID]]
  private val `Seq[String]` = TypeCase[Seq[String]]

  protected trait ToSQLArray[T]:
    extension (seq: Seq[T]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array

  given ToSQLArray[BigInt] with
    extension (seq: Seq[BigInt]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("BIGINT", seq.toArray)

  given ToSQLArray[Boolean] with
    extension (seq: Seq[Boolean]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("BOOLEAN", seq.toArray)

  given ToSQLArray[Array[Byte]] with
    extension (seq: Seq[Array[Byte]]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("BYTEA", seq.map("\\x" + Hex.encodeHexString(_)).toArray)

  given ToSQLArray[LocalDate] with
    extension (seq: Seq[LocalDate]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("DATE", seq.toArray)

  given ToSQLArray[Double] with
    extension (seq: Seq[Double]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("DOUBLE", seq.toArray)

  given ToSQLArray[Float] with
    extension (seq: Seq[Float]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("REAL", seq.toArray)

  given ToSQLArray[Int] with
    extension (seq: Seq[Int]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("INT", seq.toArray)

  given ToSQLArray[Short] with
    extension (seq: Seq[Short]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("SMALLINT", seq.toArray)

  given ToSQLArray[io.circe.Json] with
    extension (seq: Seq[io.circe.Json]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      val jsonStrings = seq.map(json => json.spaces2)
      session.connection.createArrayOf("VARCHAR", jsonStrings.toArray)

  given ToSQLArray[BigDecimal] with
    extension (seq: Seq[BigDecimal]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("NUMERIC", seq.toArray)

  given ToSQLArray[LocalTime] with
    extension (seq: Seq[LocalTime]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("TIME", seq.toArray)

  given ToSQLArray[ZonedDateTime] with
    extension (seq: Seq[ZonedDateTime]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("TIMESTAMP", seq.toArray)

  given ToSQLArray[UUID] with
    extension (seq: Seq[UUID]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("VARCHAR", seq.toArray)

  given ToSQLArray[String] with
    extension (seq: Seq[String]) def toSQLArray(implicit session: DBSession = AutoSession): sql.Array =
      session.connection.createArrayOf("VARCHAR", seq.toArray)

  def generateOperatorString(operator: Operator): String = operator match {
    case ScalarOperator.greaterThan => ">"
    case ScalarOperator.greaterThanOrEqualTo => ">="
    case ScalarOperator.equalTo => "="
    case ScalarOperator.notEqualTo => "!="
    case ScalarOperator.lessThanOrEualTo => "<="
    case ScalarOperator.lessThan => "<"
    case ScalarOperator.like => "LIKE"
    case VectorOperator.greaterThanAny => "> ANY"
    case VectorOperator.greaterThanOrEqualToAny => ">= ANY"
    case VectorOperator.equalToAny => "= ANY"
    case VectorOperator.notEqualToAny => "!= ANY"
    case VectorOperator.lessThanOrEualToAny => "<= ANY"
    case VectorOperator.lessThanAny => "< ANY"
    case VectorOperator.likeAny => "LIKE ANY"
    case VectorOperator.greaterThanAll => "> ALL"
    case VectorOperator.greaterThanOrEqualToAll => ">= ALL"
    case VectorOperator.equalToAll => "= ALL"
    case VectorOperator.notEqualToAll => "!= ALL"
    case VectorOperator.lessThanOrEualToAll => "<= ALL"
    case VectorOperator.lessThanAll => "< ALL"
    case VectorOperator.likeAll => "LIKE ALL"
    case LogicalOperator.and => "INTERSECT"
    case LogicalOperator.or => "UNION"
  }

  def propertyTypeToScalarCast(propertyType: PropertyType): String = propertyType match
    case PropertyType.bytes => "bytea"
    case PropertyType.float => "real"
    case PropertyType.double => "double precision"
    case PropertyType.json => "jsonb"
    case _ => propertyType.toString

  def propertyTypeToVectorCast(propertyType: PropertyType): String =
    propertyTypeToScalarCast(propertyType) + "[]"

  def generateSqlAndParameters(propertyPK: UUID,
                               propertyTypes: Seq[PropertyType],
                               operator: String,
                               propertyTypeToCast: PropertyType => String,
                               values: Any): (String, Seq[Any]) =
    val sqlAndParameters = propertyTypes.map(propertyType =>
      val tableName = s"property_value_${propertyType.toString}"
      val cast = propertyTypeToCast(propertyType)
      (
        s"""
            SELECT pv.collection_pk
            FROM $tableName AS pv
            WHERE pv.property_pk = ?::uuid
            AND pv.property_value ${operator} (?::${cast})
           """,
        Seq(propertyPK, values)
      )
    )
    sqlAndParameters.reduce {
      case ((sqlA, parametersA), (sqlB, parametersB)) => (s"${sqlA} UNION ${sqlB}", parametersA ++ parametersB)
    }

  def comparisonToSQL(comparison: Comparison)(implicit session: DBSession = AutoSession): (String, Seq[Any]) = comparison match
    case PropertyValueScalarComparison(lhs, operator, rhs) =>
      val value = rhs match {
        case json: io.circe.Json => json.spaces2
        case _ => rhs
      }
      generateSqlAndParameters(lhs.pk, lhs.propertyTypes, generateOperatorString(operator), propertyTypeToScalarCast, value)

    case PropertyValueVectorComparison(lhs, operator, rhs) =>
      val values = rhs match {
        case `Seq[BigInt]`(seq) => seq.toSQLArray
        case `Seq[Boolean]`(seq) => seq.toSQLArray
        case `Seq[Array[Byte]]`(seq) => seq.toSQLArray
        case `Seq[LocalDate]`(seq) => seq.toSQLArray
        case `Seq[Double]`(seq) => seq.toSQLArray
        case `Seq[Float]`(seq) => seq.toSQLArray
        case `Seq[Int]`(seq) => seq.toSQLArray
        case `Seq[Short]`(seq) => seq.toSQLArray
        case `Seq[io.circe.Json]`(seq) => seq.toSQLArray
        case `Seq[BigDecimal]`(seq) => seq.toSQLArray
        case `Seq[LocalTime]`(seq) => seq.toSQLArray
        case `Seq[ZonedDateTime]`(seq) => seq.toSQLArray
        case `Seq[UUID]`(seq) => seq.toSQLArray
        case `Seq[String]`(seq) => seq.toSQLArray
        case _ => session.connection.createArrayOf("VARCHAR", Array.empty) // Included to suppress compiler warnings
      }
      generateSqlAndParameters(lhs.pk, lhs.propertyTypes, generateOperatorString(operator), propertyTypeToVectorCast, values)

    case PropertyPropertyComparison(lhs, operator, rhs) =>
      // TODO: Implement
      ("", Nil)

    case NestedComparison(lhs, operator, rhs) =>
      val (lhsSQL, lhsParameters) = comparisonToSQL(lhs)
      val (rhsSQL, rhsParameters) = comparisonToSQL(rhs)
      val operatorString = generateOperatorString(operator)
      (s"($lhsSQL) $operatorString ($rhsSQL)", lhsParameters ++ rhsParameters)

  def comparisonsToSQL(comparisons: Seq[Comparison])(implicit sessions: DBSession = AutoSession): Option[(String, Seq[Any])] =
    comparisons.reduceOption((c1, c2) => c1 and c2).map(comparisonToSQL)
