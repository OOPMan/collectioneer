package com.oopman.collectioneer.db.h2.entity.projected

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.projected.Collection

import java.time.ZonedDateTime
import java.util.UUID

case class Collection
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  properties: List[Property] = Nil,
  propertyValues: List[PropertyValue] = Nil
  // TODO: Add a property for related Collections
) extends traits.entity.projected.Collection
