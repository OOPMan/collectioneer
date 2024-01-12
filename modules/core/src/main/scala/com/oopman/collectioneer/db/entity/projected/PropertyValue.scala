package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.entity.Utils.{resultSetArrayToListOf, resultSetArrayToPropertyTypeList}
import scalikejdbc.*

import java.sql.{Blob, Clob}
import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID
import scala.reflect.ClassTag

case class PropertyValue
(
  property: Property = Property(),
  propertyValueSetPk: UUID,
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
):

  def toRawPropertyValues: List[traits.entity.PropertyValue[?]] =
    varcharValues.map(stringValue => entity.raw.propertyvalue.PropertyValueVarchar(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = stringValue
    )) ++
    varbinaryValues.map(byteValues => entity.raw.propertyvalue.PropertyValueVarbinary(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = byteValues
    )) ++
    tinyintValues.map(byteValue => entity.raw.propertyvalue.PropertyValueTinyint(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = byteValue
    )) ++
    smallintValues.map(intValue => entity.raw.propertyvalue.PropertyValueSmallint(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = intValue
    )) ++
    intValues.map(intValue => entity.raw.propertyvalue.PropertyValueInt(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = intValue
    )) ++
    bigintValues.map(bigIntValue => entity.raw.propertyvalue.PropertyValueBigint(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = bigIntValue
    )) ++
    numericValues.map(numericValue => entity.raw.propertyvalue.PropertyValueNumeric(
        propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = numericValue
    )) ++
    floatValues.map(floatValue => entity.raw.propertyvalue.PropertyValueFloat(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = floatValue
    )) ++
    doubleValues.map(doubleValue => entity.raw.propertyvalue.PropertyValueDouble(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = doubleValue
    )) ++
    booleanValues.map(booleanValue => entity.raw.propertyvalue.PropertyValueBoolean(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = booleanValue
    )) ++
    dateValues.map(dateValue => entity.raw.propertyvalue.PropertyValueDate(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = dateValue
    )) ++
    timeValues.map(timeValue => entity.raw.propertyvalue.PropertyValueTime(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = timeValue
    )) ++
    timestampValues.map(timestampValue => entity.raw.propertyvalue.PropertyValueTimestamp(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = timestampValue
    )) ++
    clobValues.map(clobValue => entity.raw.propertyvalue.PropertyValueCLOB(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = clobValue
    )) ++
    blobValues.map(blobValue => entity.raw.propertyvalue.PropertyValueBLOB(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = blobValue
    )) ++
    uuidValues.map(uuidValue => entity.raw.propertyvalue.PropertyValueUUID(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = uuidValue
    )) ++
    jsonValues.map(jsonValue => entity.raw.propertyvalue.PropertyValueJSON(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = jsonValue
    ))

object PropertyValue:
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
    PropertyValue(
      property = Property(
        pk = UUID.fromString(rs.string("PROPERTY_PK")),
        propertyName = rs.string("PROPERTY_NAME"),
        propertyTypes = propertyTypes,
        deleted = rs.boolean("DELETED"),
        created = rs.zonedDateTime("CREATED"),
        modified = rs.zonedDateTime("MODIFIED"),
      ),
      propertyValueSetPk = UUID.fromString(rs.string("PROPERTY_VALUE_SET_PK")),
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
