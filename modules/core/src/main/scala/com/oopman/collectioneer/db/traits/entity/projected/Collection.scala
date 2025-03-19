package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

trait Collection extends traits.entity.raw.Collection:
  val properties: Seq[Property]
  val relatedProperties: Seq[Property]
  val propertyValues: Seq[PropertyValue]
  // TODO: Add fields for relationships
  // val relationships:List[Relationship]
