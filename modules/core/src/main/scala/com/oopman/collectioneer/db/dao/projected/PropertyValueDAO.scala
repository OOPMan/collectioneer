package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.entity.projected.{PropertyValue, generatePropertyValuesFromWrappedResultSet}
import com.oopman.collectioneer.db.queries.h2

import java.util.UUID
import scalikejdbc.*

import java.sql.Connection

object PropertyValueDAO:
  def getPropertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[PropertyValue] =
    h2.projected.PropertyValueQueries
      .propertyValuesByPropertyValueSets(pvsUUIDs)
      .map(generatePropertyValuesFromWrappedResultSet)
      .list
      .apply()

  def updatePropertyValues(propertyValues: Seq[PropertyValue])(implicit session: DBSession = AutoSession) =
    val propertyPks = propertyValues.map(_.property.pk)
    val propertyValueSetPks = propertyValues.map(_.propertyValueSetPk)
    // Delete existing PropertyValues in all property value tables by property and propertyValueSet
    h2.raw.PropertyValueQueries.propertyValueQueryObjects
      .map(_.deleteByPropertyValueSetPksAndPropertyPks.bind(propertyValueSetPks, propertyPks).execute)
  // TODO: Insert new PropertyValues into relevant property value tables by property and properyValueSet


class PropertyValueDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def getPropertyValuesByPropertyValueSet(pvsUUIDs: Seq[UUID]): List[PropertyValue] =
    dbProvider() readOnly { implicit session => PropertyValueDAO.getPropertyValuesByPropertyValueSets(pvsUUIDs) }

  def updatePropertyValues(propertyValues: Seq[PropertyValue]) =
    dbProvider() localTx { implicit session => PropertyValueDAO.updatePropertyValues(propertyValues) }
