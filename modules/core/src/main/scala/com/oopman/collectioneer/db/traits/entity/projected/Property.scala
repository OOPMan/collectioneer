package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType

import java.time.ZonedDateTime
import java.util.UUID

trait Property extends traits.entity.raw.Property:
  val propertyValues: List[PropertyValue]
  val propertyValueSets: List[PropertyValueSet]
