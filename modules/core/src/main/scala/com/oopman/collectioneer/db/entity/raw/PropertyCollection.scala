package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.traits.entity.raw.{PropertyCollection, PropertyCollectionRelationshipType}

import java.time.OffsetDateTime
import java.util.UUID

case class PropertyCollection
(
  propertyPK: UUID = UUID.randomUUID(),
  collectionPK: UUID = UUID.randomUUID(),
  index: Int = 0,
  propertyCollectionRelationshipType: raw.PropertyCollectionRelationshipType = raw.PropertyCollectionRelationshipType.PropertyOfCollection,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now()
) extends raw.PropertyCollection:

  def rawCopyWith(propertyPK: UUID = propertyPK,
                  collectionPK: UUID = collectionPK,
                  index: Int = index,
                  propertyCollectionRelationshipType: PropertyCollectionRelationshipType = propertyCollectionRelationshipType,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): PropertyCollection =
    copy(
      propertyPK = propertyPK, 
      collectionPK = collectionPK, 
      index = index, 
      propertyCollectionRelationshipType = propertyCollectionRelationshipType, 
      created = created, 
      modified = modified
    )
