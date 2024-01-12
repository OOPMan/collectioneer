package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.entity.projected.PropertyValue
import com.oopman.collectioneer.db.h2.queries.projected.PropertyValueQueries
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.traits

import java.util.UUID
import scalikejdbc.*

import java.sql.Connection

object PropertyValueDAO extends traits.dao.projected.PropertyValueDAO:
  def getPropertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[PropertyValue] =
    PropertyValueQueries
      .propertyValuesByPropertyValueSets(pvsUUIDs)
      .map(PropertyValue.generatePropertyValuesFromWrappedResultSet)
      .list
      .apply()

  def updatePropertyValues(propertyValues: Seq[PropertyValue])(implicit session: DBSession = AutoSession): Unit =
    val propertyPks = propertyValues.map(_.property.pk)
    val propertyValueSetPks = propertyValues.map(_.propertyValueSetPk)
    // Delete existing PropertyValues in all property value tables by property and propertyValueSet
    raw.PropertyValueQueries.propertyValueQueryObjects
      .map(_.deleteByPropertyValueSetPksAndPropertyPKs.bind(propertyValueSetPks, propertyPks).execute)
  // TODO: Insert new PropertyValues into relevant property value tables by property and properyValueSet
    val t  = propertyValues.flatMap(_.toRawPropertyValues)

