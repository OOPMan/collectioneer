package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum CollectionRelatedCollectionRelationship:
  case ParentCollection extends CollectionRelatedCollectionRelationship
  case SourceOfProperties extends CollectionRelatedCollectionRelationship
  case SourceOfChildCollections extends CollectionRelatedCollectionRelationship
  
trait CollectionRelatedCollection:
  val pk: UUID
  val collectionPK: UUID
  val relatedCollectionPK: UUID
  val relationship: CollectionRelatedCollectionRelationship
  val index: Int
  val created: ZonedDateTime
  val modified: ZonedDateTime
  
