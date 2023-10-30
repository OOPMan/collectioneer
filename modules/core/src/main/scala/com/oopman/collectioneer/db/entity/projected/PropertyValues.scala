package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity.PropertyType
import scalikejdbc.*

import java.sql.{Blob, Clob}
import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID
import scala.reflect.ClassTag

case class PropertyValues
(
  propertyName: String,
  propertyTypes: List[PropertyType],
  propertyValueSetPk: UUID,
  propertyPk: UUID,
  varcharValues: List[String],
  varbinaryValues: List[Array[Byte]],
  tinyintValues: List[Byte],
  smallintValues: List[Short],
  intValues: List[Int],
  bigintValues: List[BigInt],
  numericValues: List[BigDecimal],
  floatValues: List[Float],
  doubleValues: List[Double],
  booleanValues: List[Boolean],
  dateValues: List[LocalDate],
  timeValues: List[OffsetTime],
  timestampValues: List[ZonedDateTime],
  clobValues: List[Clob],
  blobValues: List[Blob],
  uuidValues: List[UUID],
  jsonValues: List[Array[Byte]]
  // TODO: Add fields for remaining values
)

def resultSetArray(rs: WrappedResultSet, columnLabel: String): Array[Object] = rs
  .array(columnLabel)
  .getArray
  .asInstanceOf[Array[Object]]

def resultSetArrayToList[T:ClassTag](rs: WrappedResultSet, columnLabel: String): List[T] =
  resultSetArray(rs, columnLabel)
  .map(_.asInstanceOf[T])
  .toList

def generatePropertyValuesFromWrappedResultSet(rs: WrappedResultSet) =
  val propertyTypes =
    resultSetArray(rs, "PROPERTY_TYPES")
    .map(s => PropertyType.valueOf(s.asInstanceOf[String]))
    .toList
  val varcharValues = resultSetArrayToList[String](rs, "VARCHAR_VALUES")
  val varbinaryValues = resultSetArrayToList[Array[Byte]](rs, "VARBINARY_VALUES")
  val tinyintValues = resultSetArrayToList[Byte](rs, "TINYINT_VALUES")
  val smallintValues = resultSetArrayToList[Short](rs, "SMALLINT_VALUES")
  val intValues = resultSetArrayToList[Int](rs, "INT_VALUES")
  val bigintValues = resultSetArrayToList[BigInt](rs, "BIGINT_VALUES")
  val numericValues = resultSetArrayToList[BigDecimal](rs, "NUMERIC_VALUES")
  val floatValues = resultSetArrayToList[Float](rs, "FLOAT_VALUES")
  val doubleValues = resultSetArrayToList[Double](rs, "DOUBLE_VALUES")
  val booleanValues = resultSetArrayToList[Boolean](rs, "BOOLEAN_VALUES")
  val dateValues = resultSetArrayToList[LocalDate](rs, "DATE_VALUES")
  val timeValues = resultSetArrayToList[OffsetTime](rs, "TIME_VALUES")
  val timestampValues = resultSetArrayToList[ZonedDateTime](rs, "TIMESTAMP_VALUES")
  val clobValues = resultSetArrayToList[Clob](rs, "CLOB_VALUES")
  val blobValues = resultSetArrayToList[Blob](rs, "BLOB_VALUES")
  val uuidValues = resultSetArrayToList[UUID](rs, "UUID_VALUES")
  val jsonValues = resultSetArrayToList[Array[Byte]](rs, "JSON_VALUES")
  PropertyValues(
    propertyName = rs.string("PROPERTY_NAME"),
    propertyTypes = propertyTypes,
    propertyValueSetPk = UUID.fromString(rs.string("PROPERTY_VALUE_SET_PK")),
    propertyPk = UUID.fromString(rs.string("PROPERTY_PK")),
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
