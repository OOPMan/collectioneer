package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

trait Collection:
  val pk: UUID
  val virtual: Boolean
  val deleted: Boolean
  val created: ZonedDateTime
  val modified: ZonedDateTime

