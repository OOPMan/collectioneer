package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.entity.projected.{PropertyValue, generatePropertyValuesFromWrappedResultSet}
import com.oopman.collectioneer.db.queries.h2.projected.PropertyValueQueries

import java.util.UUID
import scalikejdbc.*

object PropertyValueDAO:
  def getPropertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[PropertyValue] =
    PropertyValueQueries
      .propertyValuesByPropertyValueSets(pvsUUIDs)
      .map(generatePropertyValuesFromWrappedResultSet)
      .list
      .apply()


class PropertyValueDAO(val connectionPoolName: String):

  def getPropertyValuesByPropertyValueSet(pvsUUIDs: Seq[UUID]): List[PropertyValue] =
    NamedDB(connectionPoolName) readOnly { implicit session => PropertyValueDAO.getPropertyValuesByPropertyValueSets(pvsUUIDs) }

