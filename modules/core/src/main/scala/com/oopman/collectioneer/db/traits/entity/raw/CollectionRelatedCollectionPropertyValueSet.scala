package com.oopman.collectioneer.db.traits.entity.raw

import java.util.UUID

trait CollectionRelatedCollectionPropertyValueSet:
  val collectionRelatedCollectionPK: UUID
  val propertyValueSetPK: UUID
  val index: Int