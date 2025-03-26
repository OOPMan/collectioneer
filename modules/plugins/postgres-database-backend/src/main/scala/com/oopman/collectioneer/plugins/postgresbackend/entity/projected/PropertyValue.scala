package com.oopman.collectioneer.plugins.postgresbackend.entity.projected

import com.oopman.collectioneer.db.entity.projected.{Collection, Property, PropertyValue}
import com.oopman.collectioneer.db.scalikejdbc.entity.Utils
import com.oopman.collectioneer.db.{entity, traits}
import io.circe.*
import io.circe.parser.*
import scalikejdbc.WrappedResultSet

import java.time.{LocalDate, LocalTime, ZonedDateTime, ZoneId}
import java.util.UUID

object PropertyValue:
  case class PropertyValueData
  (
    textValues: Seq[String],
    byteValues: Seq[Array[Byte]],
    smallintValues: Seq[Short],
    intValues: Seq[Int],
    bigintValues: Seq[BigInt],
    numericValues: Seq[BigDecimal],
    floatValues: Seq[Float],
    doubleValues: Seq[Double],
    booleanValues: Seq[Boolean],
    dateValues: Seq[LocalDate],
    timeValues: Seq[LocalTime],
    timestampValues: Seq[ZonedDateTime],
    uuidValues: Seq[UUID],
    jsonValues: Seq[Json]
  )

  def generatePropertyValueData(rs: WrappedResultSet): PropertyValueData =
    PropertyValueData(
      Utils.resultSetArrayToListOf[String](rs, "property_value_text"),
      Utils.resultSetArrayToListOf[Array[Byte]](rs, "property_value_bytes"),
      Utils.resultSetArrayToListOf[Short](rs, "property_value_smallint"),
      Utils.resultSetArrayToListOf[Int](rs, "property_value_int"),
      Utils.resultSetArrayToListOf[Long](rs, "property_value_bigint").map(BigInt.apply),
      Utils.resultSetArrayToListOf[java.math.BigDecimal](rs, "property_value_numeric").map(BigDecimal.javaBigDecimal2bigDecimal),
      Utils.resultSetArrayToListOf[Float](rs, "property_value_float"),
      Utils.resultSetArrayToListOf[Double](rs, "property_value_double"),
      Utils.resultSetArrayToListOf[Boolean](rs, "property_value_boolean"),
      Utils.resultSetArrayToListOf[java.sql.Date](rs, "property_value_date").map(_.toLocalDate),
      Utils.resultSetArrayToListOf[java.sql.Time](rs, "property_value_time").map(_.toLocalTime),
      Utils.resultSetArrayToListOf[java.sql.Timestamp](rs, "property_value_timestamp").map(_.toLocalDateTime.atZone(ZoneId.systemDefault)),
      Utils.resultSetArrayToListOf[UUID](rs, "property_value_uuid"),
      Utils.resultSetArrayToListOf[String](rs, "property_value_json").map(parse).map(_.toOption).filter(_.isDefined).map(_.get)
    )

  def generatePropertyValuesFromWrappedResultSet(rs: WrappedResultSet): PropertyValue =
    val propertyTypes = Utils.resultSetArrayToPropertyTypeList(rs, "property_types")
    val propertyValueData = generatePropertyValueData(rs)
    entity.projected.PropertyValue(
        property = entity.projected.Property(
          pk = UUID.fromString(rs.string("pk")),
          propertyName = rs.string("property_name"),
          propertyTypes = propertyTypes,
          deleted = rs.boolean("deleted"),
          created = rs.zonedDateTime("created"),
          modified = rs.zonedDateTime("modified"),
        ),
        collection = entity.projected.Collection(pk = UUID.fromString(rs.string("top_level_collection_pk"))),
        textValues = propertyValueData.textValues,
        byteValues = propertyValueData.byteValues,
        smallintValues = propertyValueData.smallintValues,
        intValues = propertyValueData.intValues,
        bigintValues = propertyValueData.bigintValues,
        numericValues = propertyValueData.numericValues,
        floatValues = propertyValueData.floatValues,
        doubleValues = propertyValueData.doubleValues,
        booleanValues = propertyValueData.booleanValues,
        dateValues = propertyValueData.dateValues,
        timeValues = propertyValueData.timeValues,
        timestampValues = propertyValueData.timestampValues,
        uuidValues = propertyValueData.uuidValues,
        jsonValues = propertyValueData.jsonValues
      )

  // TODO: Move this?
  def toRawPropertyValues(propertyValue: traits.entity.projected.PropertyValue): Seq[traits.entity.raw.PropertyValue[?]] =
    propertyValue.textValues.zipWithIndex.map((stringValue, index) => entity.raw.PropertyValueText(
      collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = stringValue, index = index
    )) ++
      propertyValue.byteValues.zipWithIndex.map((byteValues, index) => entity.raw.PropertyValueBytes(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = byteValues, index = index
      )) ++
      propertyValue.smallintValues.zipWithIndex.map((intValue, index) => entity.raw.PropertyValueSmallint(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = intValue, index = index
      )) ++
      propertyValue.intValues.zipWithIndex.map((intValue, index) => entity.raw.PropertyValueInt(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = intValue, index = index
      )) ++
      propertyValue.bigintValues.zipWithIndex.map((bigIntValue, index) => entity.raw.PropertyValueBigInt(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = bigIntValue, index = index
      )) ++
      propertyValue.numericValues.zipWithIndex.map((numericValue, index) => entity.raw.PropertyValueBigDecimal(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = numericValue, index = index
      )) ++
      propertyValue.floatValues.zipWithIndex.map((floatValue, index) => entity.raw.PropertyValueFloat(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = floatValue, index = index
      )) ++
      propertyValue.doubleValues.zipWithIndex.map((doubleValue, index) => entity.raw.PropertyValueDouble(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = doubleValue, index = index
      )) ++
      propertyValue.booleanValues.zipWithIndex.map((booleanValue, index) => entity.raw.PropertyValueBoolean(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = booleanValue, index = index
      )) ++
      propertyValue.dateValues.zipWithIndex.map((dateValue, index) => entity.raw.PropertyValueDate(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = dateValue, index = index
      )) ++
      propertyValue.timeValues.zipWithIndex.map((timeValue, index) => entity.raw.PropertyValueTime(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = timeValue, index = index
      )) ++
      propertyValue.timestampValues.zipWithIndex.map((timestampValue, index) => entity.raw.PropertyValueTimestamp(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = timestampValue, index = index
      )) ++
      propertyValue.uuidValues.zipWithIndex.map((uuidValue, index) => entity.raw.PropertyValueUUID(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = uuidValue, index = index
      )) ++
      propertyValue.jsonValues.zipWithIndex.map((jsonValue, index) => entity.raw.PropertyValueJSON(
        collectionPK = propertyValue.collection.pk, propertyPK = propertyValue.property.pk, propertyValue = jsonValue, index = index
      ))

