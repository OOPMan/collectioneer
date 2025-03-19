package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String = "",
  propertyTypes: Seq[traits.entity.raw.PropertyType] = Nil,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValues: Seq[PropertyValue] = Nil,
) extends traits.entity.projected.Property
