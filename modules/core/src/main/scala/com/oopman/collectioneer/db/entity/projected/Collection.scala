package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

case class Collection
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  properties: List[traits.entity.projected.Property] = Nil,
  relatedProperties: List[traits.entity.projected.Property] = Nil,
  propertyValues: List[traits.entity.projected.PropertyValue] = Nil
) extends traits.entity.projected.Collection

