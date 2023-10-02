package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.entity.projected.{PropertyValues, generatePropertyValuesFromWrappedResultSet}
import com.oopman.collectioneer.db.queries.h2.projected.PropertyValueQueries

import java.util.UUID
import scalikejdbc.*

object PropertyValuesDAO:
  def getPropertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[PropertyValues] =
    PropertyValueQueries
      .propertyValuesByPropertyValueSets(pvsUUIDs)
      .map(generatePropertyValuesFromWrappedResultSet)
      .list
      .apply()


class PropertyValuesDAO(val connectionPoolName: String):

  def getPropertyValuesByPropertyValueSet(pvsUUIDs: Seq[UUID]): List[PropertyValues] =
    NamedDB(connectionPoolName) readOnly { implicit session => PropertyValuesDAO.getPropertyValuesByPropertyValueSets(pvsUUIDs) }

