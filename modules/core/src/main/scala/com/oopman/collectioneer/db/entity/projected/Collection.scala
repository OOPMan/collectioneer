package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

class Collection
(
  val pk: UUID = UUID.randomUUID(),
  val virtual: Boolean = false,
  val deleted: Boolean = false,
  val created: ZonedDateTime = ZonedDateTime.now(),
  val modified: ZonedDateTime = ZonedDateTime.now(),
  val propertyValueSets: List[PropertyValueSet] = Nil,
) extends traits.entity.projected.Collection

