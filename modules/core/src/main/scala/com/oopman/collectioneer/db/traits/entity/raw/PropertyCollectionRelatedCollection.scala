package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyCollectionRelatedCollectionRelationship:
  case PropertyOfRelationship extends PropertyCollectionRelatedCollectionRelationship
  case PropertyOfCollection extends PropertyCollectionRelatedCollectionRelationship
  case PropertyOfRelatedCollection extends PropertyCollectionRelatedCollectionRelationship

trait PropertyCollectionRelatedCollection:
  val propertyPK: UUID
  val collectionsRelatedCollectionsPK: UUID
  val propertyValueSetPK: UUID
  val relationship: PropertyCollectionRelatedCollectionRelationship
  val index: Int
  val created: ZonedDateTime
  val modified: ZonedDateTime
