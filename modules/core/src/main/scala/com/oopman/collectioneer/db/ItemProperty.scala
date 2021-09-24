package com.oopman.collectioneer.db

import java.time.ZonedDateTime

case class ItemProperty
(
  itemId: Long,
  name: String,
  version: Long,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyType: Option[String] = None,
  stringValue: Option[String] = None,
  bigIntValue: Option[Long] = None,
  doubleValue: Option[Float] = None,
  booleanValue: Option[Boolean] = None,
  timestampValue: Option[ZonedDateTime] = None

)
