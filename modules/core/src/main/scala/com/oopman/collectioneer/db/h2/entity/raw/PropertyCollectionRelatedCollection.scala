package com.oopman.collectioneer.db.h2.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyCollectionRelatedCollection
(
  propertyPK: UUID,
  collectionsRelatedCollectionsPK: UUID,
  propertyValueSetPK: UUID,
  relationship: traits.entity.raw.PropertyCollectionRelatedCollectionRelationship = traits.entity.raw.PropertyCollectionRelatedCollectionRelationship.PropertyOfRelationship,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.raw.PropertyCollectionRelatedCollection
