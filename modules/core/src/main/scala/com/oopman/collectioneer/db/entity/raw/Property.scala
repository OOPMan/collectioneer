package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.{entity, traits}

import java.time.ZonedDateTime
import java.util.UUID

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String,
  propertyTypes: List[traits.entity.raw.PropertyType],
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
) extends traits.entity.raw.Property
