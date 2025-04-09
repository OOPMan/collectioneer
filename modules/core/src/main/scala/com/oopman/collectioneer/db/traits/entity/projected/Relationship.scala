package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

trait Relationship extends raw.Relationship:
  def collection: raw.Collection
  def relatedCollection: raw.Collection
  
  def projectedCopyWith(pk: UUID = pk,
                        collectionPK: UUID = collectionPK,
                        relatedCollectionPK: UUID = relatedCollectionPK,
                        index: Int = index,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        collection: raw.Collection = collection,
                        relatedCollection: raw.Collection = relatedCollection): Relationship
