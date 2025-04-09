package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue

import java.util.UUID

trait PropertyValueDAO:
  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID], propertyUUIDs: Seq[UUID] = Nil): Seq[(UUID, UUID, UUID, PropertyValue)]
  def updatePropertyValues(propertyValues: Seq[(UUID, UUID, PropertyValue)]): Seq[Boolean]