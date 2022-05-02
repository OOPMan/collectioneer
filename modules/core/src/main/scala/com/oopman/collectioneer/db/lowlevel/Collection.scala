package com.oopman.collectioneer.db.lowlevel

import java.time.ZonedDateTime

case class Collection
(
  id: Long,
  name: String,
  description: Option[String],
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
)
