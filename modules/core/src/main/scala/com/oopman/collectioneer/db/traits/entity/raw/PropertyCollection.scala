package com.oopman.collectioneer.db.traits.entity.raw

import java.time.OffsetDateTime
import java.util.UUID

enum PropertyCollectionRelationshipType:
  case PropertyOfCollection extends PropertyCollectionRelationshipType
  case CollectionOfPropertiesOfProperty extends PropertyCollectionRelationshipType

trait PropertyCollection:
  def propertyPK: UUID
  def collectionPK: UUID
  def index: Int
  def propertyCollectionRelationshipType: PropertyCollectionRelationshipType
  def created: OffsetDateTime
  def modified: OffsetDateTime
  
  def rawCopyWith(propertyPK: UUID = propertyPK,
                  collectionPK: UUID = collectionPK,
                  index: Int = index,
                  propertyCollectionRelationshipType: PropertyCollectionRelationshipType = propertyCollectionRelationshipType,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): PropertyCollection
