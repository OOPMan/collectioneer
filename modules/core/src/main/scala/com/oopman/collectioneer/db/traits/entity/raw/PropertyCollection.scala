package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyCollectionRelationshipType:
  case PropertyOfCollection extends PropertyCollectionRelationshipType
  case CollectionOfPropertiesOfProperty extends PropertyCollectionRelationshipType

trait PropertyCollection:
  val propertyPK: UUID
  val collectionPK: UUID
  val index: Int
  val propertyCollectionRelationshipType: PropertyCollectionRelationshipType
  val created: ZonedDateTime
  val modified: ZonedDateTime
