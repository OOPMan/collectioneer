package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

trait Relationship extends traits.entity.raw.Relationship:
  val collection: Collection
  val relatedCollection: Collection
  
  def projectedCopyWith(pk: UUID = pk,
                        collectionPK: UUID = collectionPK,
                        relatedCollectionPK: UUID = relatedCollectionPK,
                        index: Int = index,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        collection: Collection = collection,
                        relatedCollection: Collection = relatedCollection): Relationship
