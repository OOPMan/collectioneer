package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

trait RelationshipCollection extends traits.entity.raw.RelationshipCollection:
  def relationship: Relationship
  def collection: Collection
  
  def projectedCopyWith(relationshipPK: UUID = relationshipPK,
                        collectionPK: UUID = collectionPK,
                        index: Int = index,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        relationship: Relationship = relationship,
                        collection: Collection = collection): RelationshipCollection
