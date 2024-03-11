package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.{CollectionRelatedCollection, PropertyValueSet}
import scalikejdbc.{AutoSession, DBSession}

trait CollectionRelatedCollectionPropertyValueSetDAO:
  def associateCollectionRelatedCollectionWithPropertyValueSet(collectionRelatedCollection: CollectionRelatedCollection, propertyValueSet: PropertyValueSet, index: Int)(implicit session: DBSession = AutoSession): Int
