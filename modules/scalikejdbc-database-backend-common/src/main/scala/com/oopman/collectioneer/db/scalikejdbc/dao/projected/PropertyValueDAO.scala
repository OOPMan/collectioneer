package com.oopman.collectioneer.db.scalikejdbc.dao.projected

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.DatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue

import java.util.UUID


class PropertyValueDAO(val dbProvider: DBConnectionProvider, val db: DatabaseBackend) extends traits.dao.projected.PropertyValueDAO:

  def getPropertyValuesByCollectionUUIDs(pvsUUIDs: Seq[UUID]): List[PropertyValue] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyValueDAO.getPropertyValuesByCollectionUUIDs(pvsUUIDs) }

  def updatePropertyValues(propertyValues: Seq[PropertyValue]): Seq[Boolean] =
    dbProvider() localTx { implicit session => db.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues) }
