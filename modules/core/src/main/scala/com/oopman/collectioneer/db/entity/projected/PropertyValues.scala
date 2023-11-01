package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity.PropertyType
import com.oopman.collectioneer.db.entity.Utils.{resultSetArrayToListOf, resultSetArrayToPropertyTypeList}
import scalikejdbc.*

import java.sql.{Blob, Clob}
import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID
import scala.reflect.ClassTag

case class PropertyValues
(
  propertyPk: UUID,
  propertyValueSetPk: UUID,
  propertyTypes: List[PropertyType] = Nil,
  propertyName: String = "",
  varcharValues: List[String] = Nil,
  varbinaryValues: List[Array[Byte]] = Nil,
  tinyintValues: List[Byte] = Nil,
  smallintValues: List[Short] = Nil,
  intValues: List[Int] = Nil,
  bigintValues: List[BigInt] = Nil,
  numericValues: List[BigDecimal] = Nil,
  floatValues: List[Float] = Nil,
  doubleValues: List[Double] = Nil,
  booleanValues: List[Boolean] = Nil,
  dateValues: List[LocalDate] = Nil,
  timeValues: List[OffsetTime] = Nil,
  timestampValues: List[ZonedDateTime] = Nil,
  clobValues: List[Clob] = Nil,
  blobValues: List[Blob] = Nil,
  uuidValues: List[UUID] = Nil,
  jsonValues: List[Array[Byte]] = Nil
)


def generatePropertyValuesFromWrappedResultSet(rs: WrappedResultSet) =
  val propertyTypes = resultSetArrayToPropertyTypeList(rs, "PROPERTY_TYPES")
  val varcharValues = resultSetArrayToListOf[String](rs, "VARCHAR_VALUES")
  val varbinaryValues = resultSetArrayToListOf[Array[Byte]](rs, "VARBINARY_VALUES")
  val tinyintValues = resultSetArrayToListOf[Byte](rs, "TINYINT_VALUES")
  val smallintValues = resultSetArrayToListOf[Short](rs, "SMALLINT_VALUES")
  val intValues = resultSetArrayToListOf[Int](rs, "INT_VALUES")
  val bigintValues = resultSetArrayToListOf[BigInt](rs, "BIGINT_VALUES")
  val numericValues = resultSetArrayToListOf[BigDecimal](rs, "NUMERIC_VALUES")
  val floatValues = resultSetArrayToListOf[Float](rs, "FLOAT_VALUES")
  val doubleValues = resultSetArrayToListOf[Double](rs, "DOUBLE_VALUES")
  val booleanValues = resultSetArrayToListOf[Boolean](rs, "BOOLEAN_VALUES")
  val dateValues = resultSetArrayToListOf[LocalDate](rs, "DATE_VALUES")
  val timeValues = resultSetArrayToListOf[OffsetTime](rs, "TIME_VALUES")
  val timestampValues = resultSetArrayToListOf[ZonedDateTime](rs, "TIMESTAMP_VALUES")
  val clobValues = resultSetArrayToListOf[Clob](rs, "CLOB_VALUES")
  val blobValues = resultSetArrayToListOf[Blob](rs, "BLOB_VALUES")
  val uuidValues = resultSetArrayToListOf[UUID](rs, "UUID_VALUES")
  val jsonValues = resultSetArrayToListOf[Array[Byte]](rs, "JSON_VALUES")
  PropertyValues(
    propertyPk = UUID.fromString(rs.string("PROPERTY_PK")),
    propertyValueSetPk = UUID.fromString(rs.string("PROPERTY_VALUE_SET_PK")),
    propertyTypes = propertyTypes,
    propertyName = rs.string("PROPERTY_NAME"),
    varcharValues = varcharValues,
    varbinaryValues = varbinaryValues,
    tinyintValues = tinyintValues,
    smallintValues = smallintValues,
    intValues = intValues,
    bigintValues = bigintValues,
    numericValues = numericValues,
    floatValues = floatValues,
    doubleValues = doubleValues,
    booleanValues = booleanValues,
    dateValues = dateValues,
    timeValues = timeValues,
    timestampValues = timestampValues,
    clobValues = clobValues,
    blobValues = blobValues,
    uuidValues = uuidValues,
    jsonValues = jsonValues
  )
