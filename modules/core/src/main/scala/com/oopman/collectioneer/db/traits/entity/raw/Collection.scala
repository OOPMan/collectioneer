package com.oopman.collectioneer.db.traits.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

trait Collection:
  val pk: UUID
  val virtual: Boolean
  val deleted: Boolean
  val created: ZonedDateTime
  val modified: ZonedDateTime
  
  def rawCopyWith(pk: UUID = pk,
                  virtual: Boolean = virtual,
                  deleted: Boolean = deleted,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): Collection

