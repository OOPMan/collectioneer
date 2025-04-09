package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

trait PropertyCollection extends raw.PropertyCollection:
  def collection: raw.Collection
  def property: raw.Property
  
  def projectedCopyWith(propertyPK: UUID = propertyPK,
                        collectionPK: UUID = collectionPK,
                        index: Int = index,
                        propertyCollectionRelationshipType: raw.PropertyCollectionRelationshipType = propertyCollectionRelationshipType,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        collection: raw.Collection = collection,
                        property: raw.Property = property
                       ): PropertyCollection
    
