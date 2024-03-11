package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.{DBConnectionProvider, entity, traits}
import scalikejdbc._

import java.util.UUID


class PropertyValueDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def getPropertyValuesByPropertyValueSet(pvsUUIDs: Seq[UUID]): List[traits.entity.projected.PropertyValue] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyValueDAO.getPropertyValuesByPropertyValueSets(pvsUUIDs) }

  def updatePropertyValues(propertyValues: Seq[traits.entity.projected.PropertyValue]): Seq[Boolean] =
    dbProvider() localTx { implicit session => db.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues) }
