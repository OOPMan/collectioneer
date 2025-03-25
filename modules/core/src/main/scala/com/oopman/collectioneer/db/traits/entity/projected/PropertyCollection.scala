package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollectionRelationshipType

import java.time.ZonedDateTime
import java.util.UUID

trait PropertyCollection extends traits.entity.raw.PropertyCollection:
  def collection: Collection
  def property: Property
  
  def projectedCopyWith(propertyPK: UUID = propertyPK,
                        collectionPK: UUID = collectionPK,
                        index: Int = index,
                        propertyCollectionRelationshipType: PropertyCollectionRelationshipType = propertyCollectionRelationshipType,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        collection: Collection = collection,
                        property: Property = property
                       ): PropertyCollection
    
