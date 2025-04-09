package com.oopman.collectioneer.db.scalikejdbc.dao.projected

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue

import java.util.UUID


class PropertyValueDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.projected.PropertyValueDAO:

  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID], propertyUUIDs: Seq[UUID] = Nil): Seq[(UUID, UUID, Seq[UUID], PropertyValue)] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyValueDAO.getPropertyValuesByCollectionUUIDs(collectionUUIDs, propertyUUIDs) }

  def updatePropertyValues(propertyValues: Seq[(UUID, UUID, PropertyValue)]): Seq[Boolean] =
    dbProvider() localTx { implicit session => db.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues) }
