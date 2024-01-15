package com.oopman.collectioneer.db.h2.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyCollection
(
  propertyPK: UUID,
  collectionPK: UUID,
  propertyValueSetPK: UUID,
  index: Int = 0,
  relationship: traits.entity.raw.PropertyCollectionRelationship = traits.entity.raw.PropertyCollectionRelationship.PropertyOfCollection,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.raw.PropertyCollection
