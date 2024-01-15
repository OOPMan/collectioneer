package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.{h2, traits}

import java.sql.Connection
import java.util.UUID
import scalikejdbc.*


object PropertyValueDAO extends traits.dao.projected.PropertyValueDAO:
  def getPropertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[traits.entity.projected.PropertyValue] =
    h2.queries.projected.PropertyValueQueries
      .propertyValuesByPropertyValueSets(pvsUUIDs)
      .map(h2.entity.projected.PropertyValue.generatePropertyValuesFromWrappedResultSet)
      .list
      .apply()

  def updatePropertyValues(propertyValues: Seq[traits.entity.projected.PropertyValue])(implicit session: DBSession = AutoSession): Unit =
    val propertyPks = propertyValues.map(_.property.pk)
    val propertyValueSetPks = propertyValues.map(_.propertyValueSetPk)
    // Delete existing PropertyValues in all property value tables by property and propertyValueSet
    h2.queries.raw.PropertyValueQueries.propertyValueQueryObjects
      .map(_.deleteByPropertyValueSetPksAndPropertyPKs.bind(propertyValueSetPks, propertyPks).execute)
  // TODO: Insert new PropertyValues into relevant property value tables by property and properyValueSet
//    val t  = propertyValues.flatMap(_.toRawPropertyValues)

