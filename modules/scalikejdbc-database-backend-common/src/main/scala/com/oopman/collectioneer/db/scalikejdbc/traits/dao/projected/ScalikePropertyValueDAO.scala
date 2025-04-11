package com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikePropertyValueDAO:
  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID], propertyUUIDs: Seq[UUID] = Nil)(implicit session: DBSession): Seq[(UUID, UUID, Seq[UUID], PropertyValue)]
  def updatePropertyValues(propertyValues: Seq[(UUID, UUID, PropertyValue)])(implicit session: DBSession): Seq[Boolean]