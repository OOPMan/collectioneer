package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID


case class PropertyCollection
(
  propertyPK: UUID,
  collectionPK: UUID,
  propertyValueSetPK: UUID,
  index: Int = 0,
  relationship: traits.entity.PropertyCollectionRelationship = traits.entity.PropertyCollectionRelationship.PropertyOfCollection,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.PropertyCollection
