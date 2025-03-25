package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyCollectionRelationshipType:
  case PropertyOfCollection extends PropertyCollectionRelationshipType
  case CollectionOfPropertiesOfProperty extends PropertyCollectionRelationshipType

trait PropertyCollection:
  def propertyPK: UUID
  def collectionPK: UUID
  def index: Int
  def propertyCollectionRelationshipType: PropertyCollectionRelationshipType
  def created: ZonedDateTime
  def modified: ZonedDateTime
  
  def rawCopyWith(propertyPK: UUID = propertyPK,
                  collectionPK: UUID = collectionPK,
                  index: Int = index,
                  propertyCollectionRelationshipType: PropertyCollectionRelationshipType = propertyCollectionRelationshipType,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): PropertyCollection
