package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

trait Collection extends traits.entity.raw.Collection:
  val propertyValueSets: List[PropertyValueSet]
  // TODO: Add a property for related Collections
