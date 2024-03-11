package com.oopman.collectioneer.db.traits.dao.raw

import scalikejdbc.{DBSession, AutoSession}
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, PropertyValueSet}

trait CollectionPropertyValueSetDAO:
  def associateCollectionWithPropertyValueSet(collection: Collection, propertyValueSet: PropertyValueSet, index: Int)(implicit session: DBSession = AutoSession): Int
