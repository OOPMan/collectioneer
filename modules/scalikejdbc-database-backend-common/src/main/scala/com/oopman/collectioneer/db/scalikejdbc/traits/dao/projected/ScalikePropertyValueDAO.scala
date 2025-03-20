package com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikePropertyValueDAO:
  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID], propertyUUIDs: Seq[UUID] = Nil)(implicit session: DBSession): Seq[PropertyValue]
  def updatePropertyValues(propertyValues: Seq[PropertyValue])(implicit session: DBSession): Seq[Boolean]