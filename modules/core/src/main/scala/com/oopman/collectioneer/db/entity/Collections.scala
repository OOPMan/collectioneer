package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

case class Collections
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
)
