package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

trait Collection extends traits.entity.raw.Collection:
  val properties: List[Property]
  val propertyValues: List[PropertyValue]
  // TODO: Add fields for relationships
  // val relationships:List[Relationship]
