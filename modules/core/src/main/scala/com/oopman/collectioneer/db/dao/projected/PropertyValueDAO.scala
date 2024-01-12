package com.oopman.collectioneer.db.dao.projected

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.traits.DatabaseBackend
import scalikejdbc.*

import java.sql.Connection
import java.util.UUID


class PropertyValueDAO(val dbProvider: () => DBConnection, db: traits.DatabaseBackend):
  def this(connectionPoolName: String, db: traits.DatabaseBackend) =
    this(() => NamedDB(connectionPoolName), db)

  def this(connection: Connection, autoclose: Boolean = false, db: traits.DatabaseBackend) =
    this(() => DB(connection).autoClose(autoclose), db)

  def getPropertyValuesByPropertyValueSet(pvsUUIDs: Seq[UUID]): List[entity.projected.PropertyValue] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyValueDAO.getPropertyValuesByPropertyValueSets(pvsUUIDs) }
//
//  def updatePropertyValues(propertyValues: Seq[entity.projected.PropertyValue]) =
//    dbProvider() localTx { implicit session => db.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues) }
