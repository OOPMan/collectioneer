package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.traits.entity.raw.{PropertyCollection, PropertyCollectionRelationshipType}

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyCollection
(
  propertyPK: UUID = UUID.randomUUID(),
  collectionPK: UUID = UUID.randomUUID(),
  index: Int = 0,
  propertyCollectionRelationshipType: raw.PropertyCollectionRelationshipType = raw.PropertyCollectionRelationshipType.PropertyOfCollection,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends raw.PropertyCollection:

  def rawCopyWith(propertyPK: UUID = propertyPK,
                  collectionPK: UUID = collectionPK,
                  index: Int = index,
                  propertyCollectionRelationshipType: PropertyCollectionRelationshipType = propertyCollectionRelationshipType,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): PropertyCollection =
    copy(
      propertyPK = propertyPK, 
      collectionPK = collectionPK, 
      index = index, 
      propertyCollectionRelationshipType = propertyCollectionRelationshipType, 
      created = created, 
      modified = modified
    )
