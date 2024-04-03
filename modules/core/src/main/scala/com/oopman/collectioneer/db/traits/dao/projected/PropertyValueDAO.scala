package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue

import java.util.UUID

trait PropertyValueDAO:
  def getPropertyValuesByCollectionUUIDs(collectionUUIDs: Seq[UUID]): List[PropertyValue]
  def updatePropertyValues(propertyValues: Seq[PropertyValue]): Seq[Boolean]