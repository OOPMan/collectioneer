package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

trait PropertyValueSet extends traits.entity.raw.PropertyValueSet:
  val properties: Seq[Property]
  val propertyValues: Seq[PropertyValue]
