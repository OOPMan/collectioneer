package com.oopman.collectioneer.db.lowlevel

import java.time.ZonedDateTime

case class CollectionProperty
(
  collectionId: Long,
  name: String,
  version: Long,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyType: String = "string",
  stringValue: Option[String] = None,
  bigIntValue: Option[Long] = None,
  doubleValue: Option[Float] = None,
  booleanValue: Option[Boolean] = None,
  timestampValue: Option[ZonedDateTime] = None
)
