package com.oopman.collectioneer.db.traits.entity.raw

import java.util.UUID

trait CollectionPropertyValueSet:
  val collectionPK: UUID
  val propertyValueSetPK: UUID
  val index: Int