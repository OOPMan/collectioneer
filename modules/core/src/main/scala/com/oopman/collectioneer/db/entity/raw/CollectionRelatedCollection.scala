package com.oopman.collectioneer.db.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum CollectionRelatedCollectionRelationship:
  case ParentCollection extends CollectionRelatedCollectionRelationship
  case SourceOfProperties extends CollectionRelatedCollectionRelationship
  case SourceOfChildCollections extends CollectionRelatedCollectionRelationship

case class CollectionRelatedCollection
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  relatedCollectionPK: UUID,
  relationship: CollectionRelatedCollectionRelationship = CollectionRelatedCollectionRelationship.ParentCollection,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
)
