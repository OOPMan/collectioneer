package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue
import scalikejdbc.{AutoSession, DBSession}
import com.oopman.collectioneer.db.{scalikejdbc, traits}
import com.oopman.collectioneer.plugins.postgresbackend

import java.util.UUID

object PropertyValueDAOImpl extends scalikejdbc.traits.dao.projected.ScalikePropertyValueDAO:
  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID], propertyUUIDs: Seq[UUID] = Nil)(implicit session: DBSession = AutoSession): List[PropertyValue] =
    val bindings =
      Seq(session.connection.createArrayOf("varchar", collectionUUIDs.toArray)) ++
        (if propertyUUIDs.nonEmpty then Seq(session.connection.createArrayOf("varchar", propertyUUIDs.toArray)) else Nil)
    postgresbackend.queries.projected.PropertyValueQueries
      .propertyValuesByCollectionPKs(propertyUUIDs.nonEmpty)
      .bind(bindings: _*)
      .map(postgresbackend.entity.projected.PropertyValue.generatePropertyValuesFromWrappedResultSet)
      .list
      .apply()

  def updatePropertyValues(propertyValues: Seq[PropertyValue])(implicit session: DBSession = AutoSession): Seq[Boolean] =
    val propertyPKs = propertyValues.map(_.property.pk).distinct
    val collectionPKs = propertyValues.map(_.collection.pk).distinct
    // Delete existing PropertyValues in all property value tables by property and propertyValueSet
    postgresbackend.queries.raw.PropertyValueQueries.propertyValueQueryObjects
      .map(_.deleteByCollectionPksAndPropertyPKs.bind(
        session.connection.createArrayOf("varchar", collectionPKs.toArray),
        session.connection.createArrayOf("varchar", propertyPKs.toArray)
      ).update.apply())
    // Insert new PropertyValues into relevant property value tables by property and properyValueSet
    propertyValues
      .flatMap(postgresbackend.entity.projected.PropertyValue.toRawPropertyValues)
      .map {
        case pv: traits.entity.raw.PropertyValueText => (pv, postgresbackend.queries.raw.PropertyValueVarcharQueries.insert)
        case pv: traits.entity.raw.PropertyValueBytes => (pv, postgresbackend.queries.raw.PropertyValueVarbinaryQueries.insert)
        case pv: traits.entity.raw.PropertyValueSmallint => (pv, postgresbackend.queries.raw.PropertyValueSmallintQueries.insert)
        case pv: traits.entity.raw.PropertyValueInt => (pv, postgresbackend.queries.raw.PropertyValueIntQueries.insert)
        case pv: traits.entity.raw.PropertyValueBigint => (pv, postgresbackend.queries.raw.PropertyValueBigintQueries.insert)
        case pv: traits.entity.raw.PropertyValueBigDecimal => (pv, postgresbackend.queries.raw.PropertyValueNumericQueries.insert)
        case pv: traits.entity.raw.PropertyValueFloat => (pv, postgresbackend.queries.raw.PropertyValueFloatQueries.insert)
        case pv: traits.entity.raw.PropertyValueDouble => (pv, postgresbackend.queries.raw.PropertyValueDoubleQueries.insert)
        case pv: traits.entity.raw.PropertyValueBoolean => (pv, postgresbackend.queries.raw.PropertyValueBooleanQueries.insert)
        case pv: traits.entity.raw.PropertyValueDate => (pv, postgresbackend.queries.raw.PropertyValueDateQueries.insert)
        case pv: traits.entity.raw.PropertyValueTime => (pv, postgresbackend.queries.raw.PropertyValueTimeQueries.insert)
        case pv: traits.entity.raw.PropertyValueTimestamp => (pv, postgresbackend.queries.raw.PropertyValueTimestampQueries.insert)
        case pv: traits.entity.raw.PropertyValueUUID => (pv, postgresbackend.queries.raw.PropertyValueUUIDQueries.insert)
        case pv: traits.entity.raw.PropertyValueJSON => (pv, postgresbackend.queries.raw.PropertyValueJSONQueries.insert)
      }
      .map {
        case (pv: traits.entity.raw.PropertyValueJSON, query) => query.bind(
          pv.pk, pv.collectionPK, pv.propertyPK, pv.propertyValue.spaces2, pv.index
        ).execute.apply()
        case (pv, query) => query.bind(pv.pk, pv.collectionPK, pv.propertyPK, pv.propertyValue, pv.index).execute.apply()
      }

