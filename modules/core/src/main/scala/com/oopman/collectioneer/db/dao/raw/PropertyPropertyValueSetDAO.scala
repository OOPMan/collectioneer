package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.{DBConnectionProvider, traits}

class PropertyPropertyValueSetDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):
  def associatePropertyWithPropertyValueSet
  (
    property: traits.entity.raw.Property, 
    propertyValueSet: traits.entity.raw.PropertyValueSet, 
    relationship: traits.entity.raw.PropertyPropertyValueSetRelationship, 
    index: Int
  ): Boolean =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyPropertyValueSetDAO.associatePropertyWithPropertyValueSet(property, propertyValueSet, relationship, index) }

  def createOrUpdatePropertyPropertyValueSets(propertyPropertyValueSets: Seq[traits.entity.raw.PropertyPropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyPropertyValueSetDAO.createOrUpdatePropertyPropertyValueSets(propertyPropertyValueSets) }
