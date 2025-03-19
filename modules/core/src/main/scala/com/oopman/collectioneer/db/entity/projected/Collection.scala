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
  properties: Seq[traits.entity.projected.Property] = Nil,
  relatedProperties: Seq[traits.entity.projected.Property] = Nil,
  propertyValues: Seq[traits.entity.projected.PropertyValue] = Nil
) extends traits.entity.projected.Collection

