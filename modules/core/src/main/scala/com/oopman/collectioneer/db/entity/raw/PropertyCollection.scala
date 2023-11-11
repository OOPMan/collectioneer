package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.entity
import java.time.ZonedDateTime
import java.util.UUID


case class PropertyCollection
(
  propertyPK: UUID,
  collectionPK: UUID,
  propertyValueSetPK: UUID,
  index: Int = 0,
  relationship: entity.PropertyCollectionRelationship = entity.PropertyCollectionRelationship.PropertyOfCollection,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends entity.PropertyCollection
