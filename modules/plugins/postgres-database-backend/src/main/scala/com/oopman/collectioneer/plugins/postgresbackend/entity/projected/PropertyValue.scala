package com.oopman.collectioneer.plugins.postgresbackend.entity.projected

import com.oopman.collectioneer.db.scalikejdbc.entity.Utils
import com.oopman.collectioneer.db.{entity, traits}
import io.circe.parser.*
import scalikejdbc.WrappedResultSet

import java.time.{LocalDate, LocalTime, OffsetDateTime}
import java.util.UUID

object PropertyValue:

  def generatePropertyValueData(rs: WrappedResultSet): traits.entity.projected.PropertyValue =
    entity.projected.PropertyValue(
      Utils.resultSetArrayToListOf[String](rs, "property_value_string"),
      Utils.resultSetArrayToListOf[Array[Byte]](rs, "property_value_bytes"),
      Utils.resultSetArrayToListOf[Short](rs, "property_value_short"),
      Utils.resultSetArrayToListOf[Int](rs, "property_value_int"),
      Utils.resultSetArrayToListOf[Long](rs, "property_value_long"),
      Utils.resultSetArrayToListOf[Float](rs, "property_value_float"),
      Utils.resultSetArrayToListOf[Double](rs, "property_value_double"),
      Utils.resultSetArrayToListOf[Boolean](rs, "property_value_boolean"),
      Utils.resultSetArrayToListOf[LocalDate](rs, "property_value_localdate"),
      Utils.resultSetArrayToListOf[LocalTime](rs, "property_value_localtime"),
      Utils.resultSetArrayToListOf[OffsetDateTime](rs, "property_value_offsetdatetime"),
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
    propertyValue.stringValues.zipWithIndex.map((stringValue, index) => entity.raw.PropertyValueString(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = stringValue, index = index
    )) ++
    propertyValue.byteValues.zipWithIndex.map((byteValues, index) => entity.raw.PropertyValueBytes(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = byteValues, index = index
    )) ++
    propertyValue.shortValues.zipWithIndex.map((intValue, index) => entity.raw.PropertyValueShort(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = intValue, index = index
    )) ++
    propertyValue.intValues.zipWithIndex.map((intValue, index) => entity.raw.PropertyValueInt(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = intValue, index = index
    )) ++
    propertyValue.longValues.zipWithIndex.map((bigIntValue, index) => entity.raw.PropertyValueLong(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = bigIntValue, index = index
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
    propertyValue.localDateValues.zipWithIndex.map((dateValue, index) => entity.raw.PropertyValueLocalDate(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = dateValue, index = index
    )) ++
    propertyValue.localTimeValues.zipWithIndex.map((timeValue, index) => entity.raw.PropertyValueLocalTime(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = timeValue, index = index
    )) ++
    propertyValue.offsetDateTimeValues.zipWithIndex.map((timestampValue, index) => entity.raw.PropertyValueOffsetDateTime(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = timestampValue, index = index
    )) ++
    propertyValue.uuidValues.zipWithIndex.map((uuidValue, index) => entity.raw.PropertyValueUUID(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = uuidValue, index = index
    )) ++
    propertyValue.jsonValues.zipWithIndex.map((jsonValue, index) => entity.raw.PropertyValueJSON(
      collectionPK = collectionPK, propertyPK = propertyPK, propertyValue = jsonValue, index = index
    ))

