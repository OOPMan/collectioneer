package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

trait Property extends traits.entity.raw.Property:
  val propertyValues: List[PropertyValue]
  val collections: List[Collection]
