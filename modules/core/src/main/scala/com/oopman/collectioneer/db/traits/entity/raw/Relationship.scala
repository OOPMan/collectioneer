package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum RelationshipType:
  case ParentCollection extends RelationshipType
  case SourceOfPropertiesAndPropertyValues extends RelationshipType
  case SourceOfChildCollections extends RelationshipType

trait Relationship:
  val pk: UUID
  val collectionPK: UUID
  val relatedCollectionPK: UUID
  val relationshipType: RelationshipType
  val index: Int
  val created: ZonedDateTime
  val modified: ZonedDateTime
  
