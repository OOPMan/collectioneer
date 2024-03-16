package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyCollection
(
  propertyPK: UUID = UUID.randomUUID(),
  collectionPK: UUID = UUID.randomUUID(),
  index: Int = 0,
  propertyCollectionRelationshipType: raw.PropertyCollectionRelationshipType = raw.PropertyCollectionRelationshipType.PropertyOfCollection,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends raw.PropertyCollection
