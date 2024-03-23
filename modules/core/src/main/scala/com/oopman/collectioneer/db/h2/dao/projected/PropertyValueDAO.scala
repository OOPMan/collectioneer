package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.{entity, h2, traits}
import scalikejdbc.*

import java.util.UUID


object PropertyValueDAO extends traits.dao.projected.PropertyValueDAO:
  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[traits.entity.projected.PropertyValue] =
    h2.queries.projected.PropertyValueQueries
      .propertyValuesByCollectionPKs(collectionUUIDs)
      .map(entity.projected.PropertyValue.generatePropertyValuesFromWrappedResultSet)
      .list
      .apply()

  def updatePropertyValues(propertyValues: Seq[traits.entity.projected.PropertyValue])(implicit session: DBSession = AutoSession): Seq[Boolean] =
    val propertyPKs = propertyValues.map(_.property.pk).distinct
    val collectionPKs = propertyValues.map(_.collection.pk).distinct
    // Delete existing PropertyValues in all property value tables by property and propertyValueSet
    h2.queries.raw.PropertyValueQueries.propertyValueQueryObjects
      .map(_.deleteByCollectionPksAndPropertyPKs.bind(
        session.connection.createArrayOf("UUID", collectionPKs.toArray),
        session.connection.createArrayOf("UUID", propertyPKs.toArray)
      ).update.apply())
    // Insert new PropertyValues into relevant property value tables by property and properyValueSet
    propertyValues
      .flatMap(entity.projected.PropertyValue.toRawPropertyValues)
      .map {
        case pv: traits.entity.raw.PropertyValueVarchar => (pv, h2.queries.raw.PropertyValueVarcharQueries.insert)
        case pv: traits.entity.raw.PropertyValueVarbinary => (pv, h2.queries.raw.PropertyValueVarbinaryQueries.insert)
        case pv: traits.entity.raw.PropertyValueTinyint => (pv, h2.queries.raw.PropertyValueTinyintQueries.insert)
        case pv: traits.entity.raw.PropertyValueSmallint => (pv, h2.queries.raw.PropertyValueSmallintQueries.insert)
        case pv: traits.entity.raw.PropertyValueInt => (pv, h2.queries.raw.PropertyValueIntQueries.insert)
        case pv: traits.entity.raw.PropertyValueBigint => (pv, h2.queries.raw.PropertyValueBigintQueries.insert)
        case pv: traits.entity.raw.PropertyValueNumeric => (pv, h2.queries.raw.PropertyValueNumericQueries.insert)
        case pv: traits.entity.raw.PropertyValueFloat => (pv, h2.queries.raw.PropertyValueFloatQueries.insert)
        case pv: traits.entity.raw.PropertyValueDouble => (pv, h2.queries.raw.PropertyValueDoubleQueries.insert)
        case pv: traits.entity.raw.PropertyValueBoolean => (pv, h2.queries.raw.PropertyValueBooleanQueries.insert)
        case pv: traits.entity.raw.PropertyValueDate => (pv, h2.queries.raw.PropertyValueDateQueries.insert)
        case pv: traits.entity.raw.PropertyValueTime => (pv, h2.queries.raw.PropertyValueTimeQueries.insert)
        case pv: traits.entity.raw.PropertyValueTimestamp => (pv, h2.queries.raw.PropertyValueTimestampQueries.insert)
        case pv: traits.entity.raw.PropertyValueCLOB => (pv, h2.queries.raw.PropertyValueCLOBQueries.insert)
        case pv: traits.entity.raw.PropertyValueBLOB => (pv, h2.queries.raw.PropertyValueBLOBQueries.insert)
        case pv: traits.entity.raw.PropertyValueUUID => (pv, h2.queries.raw.PropertyValueUUIDQueries.insert)
        case pv: traits.entity.raw.PropertyValueJSON => (pv, h2.queries.raw.PropertyValueJSONQueries.insert)
      }
      .map((pv, query) => query.bind(pv.pk, pv.collectionPK, pv.propertyPK, pv.propertyValue, pv.index).execute.apply())

