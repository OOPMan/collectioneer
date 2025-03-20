package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

trait RelationshipCollection:
  val relationshipPK: UUID
  val collectionPK: UUID
  val index: Int
  val created: ZonedDateTime
  val modified: ZonedDateTime
  
  def rawCopyWith(relationshipPK: UUID = relationshipPK,
                  collectionPK: UUID = collectionPK,
                  index: Int = index,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): RelationshipCollection
