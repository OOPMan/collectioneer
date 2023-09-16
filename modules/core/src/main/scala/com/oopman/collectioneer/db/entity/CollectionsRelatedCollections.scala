package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

enum CollectionsRelatedCollectionsRelationship:
  case ParentCollection extends CollectionsRelatedCollectionsRelationship
  case SourceOfProperties extends CollectionsRelatedCollectionsRelationship
  case SourceOfChildCollections extends CollectionsRelatedCollectionsRelationship

case class CollectionsRelatedCollections
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  relatedCollectionPK: UUID,
  relationship: CollectionsRelatedCollectionsRelationship = CollectionsRelatedCollectionsRelationship.ParentCollection,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
)
