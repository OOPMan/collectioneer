package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyCollectionRelatedCollectionRelationship:
  case PropertyOfRelationship extends PropertyCollectionRelatedCollectionRelationship
  case PropertyOfCollection extends PropertyCollectionRelatedCollectionRelationship
  case PropertyOfRelatedCollection extends PropertyCollectionRelatedCollectionRelationship

case class PropertyCollectionRelatedCollection
(
  propertyPK: UUID,
  collectionsRelatedCollectionsPK: UUID,
  propertyValueSetPK: UUID,
  relationship: PropertyCollectionRelatedCollectionRelationship = PropertyCollectionRelatedCollectionRelationship.PropertyOfRelationship,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
)
