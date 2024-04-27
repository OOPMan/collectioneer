package com.oopman.collectioneer.db.scalikejdbc.dao.projected

import com.oopman.collectioneer.db.scalikejdbc.DBConnectionProvider
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.ScalikeDatabaseBackend
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue

import java.util.UUID


class PropertyValueDAOImpl(val dbProvider: DBConnectionProvider, val db: ScalikeDatabaseBackend) extends traits.dao.projected.PropertyValueDAO:

  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID], propertyUUIDs: Seq[UUID] = Nil, deleted: Seq[Boolean] = Seq(false)): List[PropertyValue] =
    dbProvider() readOnly { implicit session => db.dao.projected.PropertyValueDAO.getPropertyValuesByCollectionUUIDs(collectionUUIDs, propertyUUIDs, deleted) }

  def updatePropertyValues(propertyValues: Seq[PropertyValue]): Seq[Boolean] =
    dbProvider() localTx { implicit session => db.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues) }
