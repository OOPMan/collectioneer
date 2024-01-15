package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.{DBConnectionProvider, entity, traits}
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue
import scalikejdbc.*

import java.sql.Connection
import java.util.UUID


class PropertyValueDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def getPropertyValuesByPropertyValueSet(pvsUUIDs: Seq[UUID]): List[PropertyValue] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyValueDAO.getPropertyValuesByPropertyValueSets(pvsUUIDs) }
//
//  def updatePropertyValues(propertyValues: Seq[entity.projected.PropertyValue]) =
//    dbProvider() localTx { implicit session => db.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues) }
