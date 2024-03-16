package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue
import scalikejdbc.DBSession

import java.util.UUID

trait PropertyValueDAO:

  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID])(implicit session: DBSession): List[PropertyValue]

  def updatePropertyValues(propertyValues: Seq[PropertyValue])(implicit session: DBSession): Seq[Boolean]