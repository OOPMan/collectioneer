package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.h2.queries.raw.PropertyPropertyValueSetQueries
import com.oopman.collectioneer.db.traits.entity.raw.{Property, PropertyPropertyValueSet, PropertyPropertyValueSetRelationship, PropertyValueSet}
import com.oopman.collectioneer.db.{entity, h2, traits}
import scalikejdbc.*

object PropertyPropertyValueSetDAO extends traits.dao.raw.PropertyPropertyValueSetDAO:
  def associatePropertyWithPropertyValueSet(property: Property, propertyValueSet: PropertyValueSet, relationship: PropertyPropertyValueSetRelationship, index: Int)(implicit session: DBSession): Boolean =
    PropertyPropertyValueSetQueries
      .upsert
      .bind(property.pk.toString, propertyValueSet.pk.toString, index, relationship.toString)
      .execute
      .apply()

  def createOrUpdatePropertyPropertyValueSets(propertyPropertyValueSets: Seq[PropertyPropertyValueSet])(implicit session: DBSession): Array[Int] =
    PropertyPropertyValueSetQueries
      .upsert
      .batch(traits.entity.raw.PropertyPropertyValueSet.propertyPropertyValueSetSeqToBatchUpsertSeqSeq(propertyPropertyValueSets): _*)
      .apply()


