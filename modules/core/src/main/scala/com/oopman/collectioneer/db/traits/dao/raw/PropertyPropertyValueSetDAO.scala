package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.{Property, PropertyPropertyValueSet, PropertyPropertyValueSetRelationship, PropertyValueSet}
import scalikejdbc.{AutoSession, DBSession}

trait PropertyPropertyValueSetDAO:
  def associatePropertyWithPropertyValueSet(property: Property, propertyValueSet: PropertyValueSet, relationship: PropertyPropertyValueSetRelationship, index: Int)(implicit session: DBSession = AutoSession): Boolean

  def createOrUpdatePropertyPropertyValueSets(propertyPropertyValueSets: Seq[PropertyPropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int]
