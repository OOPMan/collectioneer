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

  def generatePropertyValueData(rs: WrappedResultSet): traits.entity.projected.PropertyValue =
    entity.projected.PropertyValue(
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

  def generatePropertyValuesFromWrappedResultSet(rs: WrappedResultSet): (UUID, UUID, Seq[UUID], traits.entity.projected.PropertyValue) =
    (
      UUID.fromString(rs.string("property_pk")),
      UUID.fromString(rs.string("top_level_collection_pk")),
      rs.array("related_collection_pk").getArray.asInstanceOf[Array[UUID]],
      generatePropertyValueData(rs)
    )

  // TODO: Move this?
  def toRawPropertyValues(propertyPK: UUID, collectionPK: UUID, propertyValue: traits.entity.projected.PropertyValue): Seq[traits.entity.raw.PropertyValue[?]] =
    propertyValue.textValues.zipWithIndex.map((stringValue, index) => entity.raw.PropertyValueText(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = stringValue, index = index
    )) ++
    propertyValue.byteValues.zipWithIndex.map((byteValues, index) => entity.raw.PropertyValueBytes(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = byteValues, index = index
    )) ++
    propertyValue.smallintValues.zipWithIndex.map((intValue, index) => entity.raw.PropertyValueSmallint(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = intValue, index = index
    )) ++
    propertyValue.intValues.zipWithIndex.map((intValue, index) => entity.raw.PropertyValueInt(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = intValue, index = index
    )) ++
    propertyValue.bigintValues.zipWithIndex.map((bigIntValue, index) => entity.raw.PropertyValueBigInt(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = bigIntValue, index = index
    )) ++
    propertyValue.numericValues.zipWithIndex.map((numericValue, index) => entity.raw.PropertyValueBigDecimal(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = numericValue, index = index
    )) ++
    propertyValue.floatValues.zipWithIndex.map((floatValue, index) => entity.raw.PropertyValueFloat(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = floatValue, index = index
    )) ++
    propertyValue.doubleValues.zipWithIndex.map((doubleValue, index) => entity.raw.PropertyValueDouble(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = doubleValue, index = index
    )) ++
    propertyValue.booleanValues.zipWithIndex.map((booleanValue, index) => entity.raw.PropertyValueBoolean(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = booleanValue, index = index
    )) ++
    propertyValue.dateValues.zipWithIndex.map((dateValue, index) => entity.raw.PropertyValueDate(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = dateValue, index = index
    )) ++
    propertyValue.timeValues.zipWithIndex.map((timeValue, index) => entity.raw.PropertyValueTime(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = timeValue, index = index
    )) ++
    propertyValue.timestampValues.zipWithIndex.map((timestampValue, index) => entity.raw.PropertyValueTimestamp(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = timestampValue, index = index
    )) ++
    propertyValue.uuidValues.zipWithIndex.map((uuidValue, index) => entity.raw.PropertyValueUUID(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = uuidValue, index = index
    )) ++
    propertyValue.jsonValues.zipWithIndex.map((jsonValue, index) => entity.raw.PropertyValueJSON(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = jsonValue, index = index
    ))

