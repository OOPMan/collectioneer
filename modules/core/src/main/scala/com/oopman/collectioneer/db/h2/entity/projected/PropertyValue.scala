package com.oopman.collectioneer.db.h2.entity.projected

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.entity.Utils.{resultSetArrayToListOf, resultSetArrayToPropertyTypeList}
import com.oopman.collectioneer.db.h2.entity.raw.propertyvalue.{PropertyValueBLOB, PropertyValueBigint, PropertyValueBoolean, PropertyValueCLOB, PropertyValueDate, PropertyValueDouble, PropertyValueFloat, PropertyValueInt, PropertyValueJSON, PropertyValueNumeric, PropertyValueSmallint, PropertyValueTime, PropertyValueTimestamp, PropertyValueTinyint, PropertyValueUUID, PropertyValueVarbinary, PropertyValueVarchar}
import com.oopman.collectioneer.db.traits.entity.raw
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
) extends traits.entity.projected.PropertyValue:

  def toRawPropertyValues: List[raw.PropertyValue[?]] =
    varcharValues.map(stringValue => PropertyValueVarchar(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = stringValue
    )) ++
    varbinaryValues.map(byteValues => PropertyValueVarbinary(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = byteValues
    )) ++
    tinyintValues.map(byteValue => PropertyValueTinyint(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = byteValue
    )) ++
    smallintValues.map(intValue => PropertyValueSmallint(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = intValue
    )) ++
    intValues.map(intValue => PropertyValueInt(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = intValue
    )) ++
    bigintValues.map(bigIntValue => PropertyValueBigint(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = bigIntValue
    )) ++
    numericValues.map(numericValue => PropertyValueNumeric(
        propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = numericValue
    )) ++
    floatValues.map(floatValue => PropertyValueFloat(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = floatValue
    )) ++
    doubleValues.map(doubleValue => PropertyValueDouble(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = doubleValue
    )) ++
    booleanValues.map(booleanValue => PropertyValueBoolean(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = booleanValue
    )) ++
    dateValues.map(dateValue => PropertyValueDate(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = dateValue
    )) ++
    timeValues.map(timeValue => PropertyValueTime(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = timeValue
    )) ++
    timestampValues.map(timestampValue => PropertyValueTimestamp(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = timestampValue
    )) ++
    clobValues.map(clobValue => PropertyValueCLOB(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = clobValue
    )) ++
    blobValues.map(blobValue => PropertyValueBLOB(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = blobValue
    )) ++
    uuidValues.map(uuidValue => PropertyValueUUID(
      propertyValueSetPK = propertyValueSetPk, propertyPK = property.pk, propertyValue = uuidValue
    )) ++
    jsonValues.map(jsonValue => PropertyValueJSON(
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
