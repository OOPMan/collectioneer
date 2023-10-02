package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyCollectionRelationship:
  case PropertyOfCollection extends PropertyCollectionRelationship
  case CollectionOfPropertiesOfProperty extends PropertyCollectionRelationship

case class PropertyCollection
(
  propertyPK: UUID,
  collectionPK: UUID,
  propertyValueSetPK: UUID,
  index: Int = 0,
  relationship: PropertyCollectionRelationship = PropertyCollectionRelationship.PropertyOfCollection,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
)
