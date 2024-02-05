package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity.Utils
import com.oopman.collectioneer.db.{entity, traits}

import java.sql.{Blob, Clob}
import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID
import scalikejdbc.*

case class PropertyValue
(
  property: Property = Property(),
  propertyValueSetPk: UUID = UUID.randomUUID(),
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
) extends traits.entity.projected.PropertyValue

object PropertyValue:
  def generatePropertyValuesFromWrappedResultSet(rs: WrappedResultSet) =
    val propertyTypes = Utils.resultSetArrayToPropertyTypeList(rs, "PROPERTY_TYPES")
    val varcharValues = Utils.resultSetArrayToListOf[String](rs, "VARCHAR_VALUES")
    val varbinaryValues = Utils.resultSetArrayToListOf[Array[Byte]](rs, "VARBINARY_VALUES")
    val tinyintValues = Utils.resultSetArrayToListOf[Byte](rs, "TINYINT_VALUES")
    val smallintValues = Utils.resultSetArrayToListOf[Short](rs, "SMALLINT_VALUES")
    val intValues = Utils.resultSetArrayToListOf[Int](rs, "INT_VALUES")
    val bigintValues = Utils.resultSetArrayToListOf[BigInt](rs, "BIGINT_VALUES")
    val numericValues = Utils.resultSetArrayToListOf[BigDecimal](rs, "NUMERIC_VALUES")
    val floatValues = Utils.resultSetArrayToListOf[Float](rs, "FLOAT_VALUES")
    val doubleValues = Utils.resultSetArrayToListOf[Double](rs, "DOUBLE_VALUES")
    val booleanValues = Utils.resultSetArrayToListOf[Boolean](rs, "BOOLEAN_VALUES")
    val dateValues = Utils.resultSetArrayToListOf[LocalDate](rs, "DATE_VALUES")
    val timeValues = Utils.resultSetArrayToListOf[OffsetTime](rs, "TIME_VALUES")
    val timestampValues = Utils.resultSetArrayToListOf[ZonedDateTime](rs, "TIMESTAMP_VALUES")
    val clobValues = Utils.resultSetArrayToListOf[Clob](rs, "CLOB_VALUES")
    val blobValues = Utils.resultSetArrayToListOf[Blob](rs, "BLOB_VALUES")
    val uuidValues = Utils.resultSetArrayToListOf[UUID](rs, "UUID_VALUES")
    val jsonValues = Utils.resultSetArrayToListOf[Array[Byte]](rs, "JSON_VALUES")
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

  // TODO: Move this?
  def toRawPropertyValues(propertyValue: traits.entity.projected.PropertyValue): List[traits.entity.raw.PropertyValue[?]] =
    propertyValue.varcharValues.zipWithIndex.map((stringValue, index) => entity.raw.propertyvalue.PropertyValueVarchar(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = stringValue, index = index
    )) ++
    propertyValue.varbinaryValues.zipWithIndex.map((byteValues, index) => entity.raw.propertyvalue.PropertyValueVarbinary(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = byteValues, index = index
    )) ++
    propertyValue.tinyintValues.zipWithIndex.map((byteValue, index) => entity.raw.propertyvalue.PropertyValueTinyint(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = byteValue, index = index
    )) ++
    propertyValue.smallintValues.zipWithIndex.map((intValue, index) => entity.raw.propertyvalue.PropertyValueSmallint(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = intValue, index = index
    )) ++
    propertyValue.intValues.zipWithIndex.map((intValue, index) => entity.raw.propertyvalue.PropertyValueInt(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = intValue, index = index
    )) ++
    propertyValue.bigintValues.zipWithIndex.map((bigIntValue, index) => entity.raw.propertyvalue.PropertyValueBigint(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = bigIntValue, index = index
    )) ++
    propertyValue.numericValues.zipWithIndex.map((numericValue, index) => entity.raw.propertyvalue.PropertyValueNumeric(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = numericValue, index = index
    )) ++
    propertyValue.floatValues.zipWithIndex.map((floatValue, index) => entity.raw.propertyvalue.PropertyValueFloat(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = floatValue, index = index
    )) ++
    propertyValue.doubleValues.zipWithIndex.map((doubleValue, index) => entity.raw.propertyvalue.PropertyValueDouble(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = doubleValue, index = index
    )) ++
    propertyValue.booleanValues.zipWithIndex.map((booleanValue, index) => entity.raw.propertyvalue.PropertyValueBoolean(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = booleanValue, index = index
    )) ++
    propertyValue.dateValues.zipWithIndex.map((dateValue, index) => entity.raw.propertyvalue.PropertyValueDate(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = dateValue, index = index
    )) ++
    propertyValue.timeValues.zipWithIndex.map((timeValue, index) => entity.raw.propertyvalue.PropertyValueTime(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = timeValue, index = index
    )) ++
    propertyValue.timestampValues.zipWithIndex.map((timestampValue, index) => entity.raw.propertyvalue.PropertyValueTimestamp(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = timestampValue, index = index
    )) ++
    propertyValue.clobValues.zipWithIndex.map((clobValue, index) => entity.raw.propertyvalue.PropertyValueCLOB(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = clobValue, index = index
    )) ++
    propertyValue.blobValues.zipWithIndex.map((blobValue, index) => entity.raw.propertyvalue.PropertyValueBLOB(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = blobValue, index = index
    )) ++
    propertyValue.uuidValues.zipWithIndex.map((uuidValue, index) => entity.raw.propertyvalue.PropertyValueUUID(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = uuidValue, index = index
    )) ++
    propertyValue.jsonValues.zipWithIndex.map((jsonValue, index) => entity.raw.propertyvalue.PropertyValueJSON(
      propertyValueSetPK = propertyValue.propertyValueSetPk, propertyPK = propertyValue.property.pk, propertyValue = jsonValue, index = index
    ))
