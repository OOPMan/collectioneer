package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueSets
(
  pk: UUID = UUID.randomUUID(),
  created: ZonedDateTime = ZonedDateTime.now()
)
