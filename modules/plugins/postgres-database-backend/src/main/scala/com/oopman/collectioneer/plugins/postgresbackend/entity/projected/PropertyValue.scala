package com.oopman.collectioneer.plugins.postgresbackend.entity.projected

import com.oopman.collectioneer.db.entity.projected.{Collection, Property}
import com.oopman.collectioneer.db.scalikejdbc.entity.Utils
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.WrappedResultSet

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID

object PropertyValue:
  def generatePropertyValuesFromWrappedResultSet(rs: WrappedResultSet) =
    val propertyTypes = Utils.resultSetArrayToPropertyTypeList(rs, "PROPERTY_TYPES")
    val textValues = Utils.resultSetArrayToListOf[String](rs, "VARCHAR_VALUES")
    val byteValues = Utils.resultSetArrayToListOf[Array[Byte]](rs, "VARBINARY_VALUES")
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
    val uuidValues = Utils.resultSetArrayToListOf[UUID](rs, "UUID_VALUES")
    val jsonValues = Utils.resultSetArrayToListOf[Array[Byte]](rs, "JSON_VALUES") // TODO: Fix to decode JSON
    entity.projected.PropertyValue(
        property = entity.projected.Property(
          pk = UUID.fromString(rs.string("PROPERTY_PK")),
          propertyName = rs.string("PROPERTY_NAME"),
          propertyTypes = propertyTypes,
          deleted = rs.boolean("DELETED"),
          created = rs.zonedDateTime("CREATED"),
          modified = rs.zonedDateTime("MODIFIED"),
        ),
        collection = entity.projected.Collection(pk = UUID.fromString(rs.string("COLLECTION_PK"))),
        textValues = textValues,
        byteValues = byteValues,
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
        uuidValues = uuidValues,
        jsonValues = Nil // TODO: Fix to decode JSON
      )

  // TODO: Move this?
  def toRawPropertyValues(propertyValue: traits.entity.projected.PropertyValue): List[traits.entity.raw.PropertyValue[?]] =
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
      propertyValue.bigintValues.zipWithIndex.map((bigIntValue, index) => entity.raw.PropertyValueBigint(
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

