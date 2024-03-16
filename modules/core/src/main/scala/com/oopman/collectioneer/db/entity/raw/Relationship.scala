package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID


case class Relationship
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  relatedCollectionPK: UUID,
  relationshipType: traits.entity.raw.RelationshipType = traits.entity.raw.RelationshipType.ParentCollection,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.raw.Relationship
