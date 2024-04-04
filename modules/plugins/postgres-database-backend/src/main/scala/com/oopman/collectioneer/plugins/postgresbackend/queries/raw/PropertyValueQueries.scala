package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.PropertyValue
import com.oopman.collectioneer.plugins.postgresbackend.entity.raw
import scalikejdbc.*

class PropertyValueQueries[T <: PropertyValue[?]](val pv: PropertyValueSQLSyntaxSupport[T]):
  def insert =
    sql"""
          INSERT INTO ${pv.table} (pk, collection_pk, property_pk, property_value, index)
          VALUES ( ?, ?, ?, ?, ? )
       """

  def upsert =
    sql"""
          INSERT INTO ${pv.table} (pk, collection_pk, property_pk, property_value, index)
          VALUES (?, ?, ?, ?, ?)
          ON CONFLICT(pk) DO UPDATE
          SET collection_pk = excluded.collection_pk, property_pk = excluded.property_pk, property_value =
          excluded.property_value, index = excluded.index, modified = now()
       """

  def deleteByPK =
    sql"""
          DELETE FROM ${pv.table}
          WHERE pk IN (?)
       """

  def deleteByCollectionPksAndPropertyPKs =
    sql"""
          DELETE FROM ${pv.table}
          WHERE collection_pk = ANY (cast(? AS uuid[]))
          AND property_pk = ANY (cast(? AS uuid[]))
       """

object PropertyValueVarcharQueries extends PropertyValueQueries(raw.PropertyValueText)
object PropertyValueVarbinaryQueries extends PropertyValueQueries(raw.PropertyValueBytes)
object PropertyValueSmallintQueries extends PropertyValueQueries(raw.PropertyValueSmallint)
object PropertyValueIntQueries extends PropertyValueQueries(raw.PropertyValueInt)
object PropertyValueBigintQueries extends PropertyValueQueries(raw.PropertyValueBigint)
object PropertyValueNumericQueries extends PropertyValueQueries(raw.PropertyValueBigDecimal)
object PropertyValueFloatQueries extends PropertyValueQueries(raw.PropertyValueFloat)
object PropertyValueDoubleQueries extends PropertyValueQueries(raw.PropertyValueDouble)
object PropertyValueBooleanQueries extends PropertyValueQueries(raw.PropertyValueBoolean)
object PropertyValueDateQueries extends PropertyValueQueries(raw.PropertyValueDate)
object PropertyValueTimeQueries extends PropertyValueQueries(raw.PropertyValueTime)
object PropertyValueTimestampQueries extends PropertyValueQueries(raw.PropertyValueTimestamp)
object PropertyValueUUIDQueries extends PropertyValueQueries(raw.PropertyValueUUID)
object PropertyValueJSONQueries extends PropertyValueQueries(raw.PropertyValueJSON)

object PropertyValueQueries:
  def propertyValueQueryObjects = List(
    PropertyValueVarcharQueries,
    PropertyValueVarbinaryQueries,
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
    PropertyValueUUIDQueries,
    PropertyValueJSONQueries
  )


