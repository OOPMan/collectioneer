package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

trait RelationshipCollection extends raw.RelationshipCollection:
  def relationship: raw.Relationship
  def collection: raw.Collection
  
  def projectedCopyWith(relationshipPK: UUID = relationshipPK,
                        collectionPK: UUID = collectionPK,
                        index: Int = index,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        relationship: raw.Relationship = relationship,
                        collection: raw.Collection = collection): RelationshipCollection
