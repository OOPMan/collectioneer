package com.oopman.collectioneer.db.h2.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID


case class CollectionRelatedCollection
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  relatedCollectionPK: UUID,
  relationship: traits.entity.raw.CollectionRelatedCollectionRelationship = traits.entity.raw.CollectionRelatedCollectionRelationship.ParentCollection,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.raw.CollectionRelatedCollection
