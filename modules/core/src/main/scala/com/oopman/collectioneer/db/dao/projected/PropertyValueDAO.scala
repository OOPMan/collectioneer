package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.entity.projected.{PropertyValue, generatePropertyValuesFromWrappedResultSet}
import com.oopman.collectioneer.db.queries.h2.projected.PropertyValueQueries

import java.util.UUID
import scalikejdbc.*

import java.sql.Connection

object PropertyValueDAO:
  def getPropertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[PropertyValue] =
    PropertyValueQueries
      .propertyValuesByPropertyValueSets(pvsUUIDs)
      .map(generatePropertyValuesFromWrappedResultSet)
      .list
      .apply()


class PropertyValueDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def getPropertyValuesByPropertyValueSet(pvsUUIDs: Seq[UUID]): List[PropertyValue] =
    dbProvider() readOnly { implicit session => PropertyValueDAO.getPropertyValuesByPropertyValueSets(pvsUUIDs) }

