package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

case class RelationshipCollection
(
  relationshipPK: UUID = UUID.randomUUID(),
  collectionPK: UUID = UUID.randomUUID(),
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.raw.RelationshipCollection
