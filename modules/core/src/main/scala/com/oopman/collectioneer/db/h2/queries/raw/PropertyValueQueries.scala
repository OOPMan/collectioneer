package com.oopman.collectioneer.db.h2.queries.raw

import scalikejdbc.*
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.entity.raw.propertyvalue
import com.oopman.collectioneer.db.traits.entity.raw.PropertyValue
import com.oopman.collectioneer.db.traits.queries.raw.PropertyValueQueries

class PropertyValueQueries[T <: PropertyValue[?]](val pv: propertyvalue.PropertyValueSQLSyntaxSupport[T]) extends traits.queries.raw.PropertyValueQueries:
  def insert =
    sql"""
          INSERT INTO ${pv.table} (pk, property_value_set_pk, property_pk, property_value, index)
          VALUES ( ?, ?, ?, ?, ? )
       """

  def upsert =
    sql"""
          MERGE INTO ${pv.table} (pk, property_value_set_pk, property_pk, property_value, index, modified)
          KEY (pk)
          VALUES (?, ?, ?, ?, ?, ?)
       """

  def deleteByPK =
    sql"""
          DELETE FROM ${pv.table}
          WHERE PK IN (?)
       """

  def deleteByPropertyValueSetPksAndPropertyPKs =
    sql"""
          DELETE FROM ${pv.table}
          WHERE PROPERTY_VALUE_SET_PK IN (?)
          AND PROPERTY_PK IN (?)
       """

object PropertyValueVarcharQueries extends PropertyValueQueries(propertyvalue.PropertyValueVarchar)
object PropertyValueVarbinaryQueries extends PropertyValueQueries(propertyvalue.PropertyValueVarbinary)
object PropertyValueTinyintQueries extends PropertyValueQueries(propertyvalue.PropertyValueTinyint)
object PropertyValueSmallintQueries extends PropertyValueQueries(propertyvalue.PropertyValueSmallint)
object PropertyValueIntQueries extends PropertyValueQueries(propertyvalue.PropertyValueInt)
object PropertyValueBigintQueries extends PropertyValueQueries(propertyvalue.PropertyValueBigint)
object PropertyValueNumericQueries extends PropertyValueQueries(propertyvalue.PropertyValueNumeric)
object PropertyValueFloatQueries extends PropertyValueQueries(propertyvalue.PropertyValueFloat)
object PropertyValueDoubleQueries extends PropertyValueQueries(propertyvalue.PropertyValueDouble)
object PropertyValueBooleanQueries extends PropertyValueQueries(propertyvalue.PropertyValueBoolean)
object PropertyValueDateQueries extends PropertyValueQueries(propertyvalue.PropertyValueDate)
object PropertyValueTimeQueries extends PropertyValueQueries(propertyvalue.PropertyValueTime)
object PropertyValueTimestampQueries extends PropertyValueQueries(propertyvalue.PropertyValueTimestamp)
object PropertyValueCLOBQueries extends PropertyValueQueries(propertyvalue.PropertyValueCLOB)
object PropertyValueBLOBQueries extends PropertyValueQueries(propertyvalue.PropertyValueBLOB)
object PropertyValueUUIDQueries extends PropertyValueQueries(propertyvalue.PropertyValueUUID)
object PropertyValueJSONQueries extends PropertyValueQueries(propertyvalue.PropertyValueJSON)

object PropertyValueQueries:
  def propertyValueQueryObjects = List(
    PropertyValueVarcharQueries,
    PropertyValueVarbinaryQueries,
    PropertyValueTinyintQueries,
    PropertyValueSmallintQueries,
    PropertyValueIntQueries,
    PropertyValueBigintQueries,
    PropertyValueNumericQueries,
    PropertyValueFloatQueries,
    PropertyValueDoubleQueries,
    PropertyValueBooleanQueries,
    PropertyValueDateQueries,
    PropertyValueTimeQueries,
    PropertyValueTimestampQueries,
    PropertyValueCLOBQueries,
    PropertyValueBLOBQueries,
    PropertyValueUUIDQueries,
    PropertyValueJSONQueries
  )


