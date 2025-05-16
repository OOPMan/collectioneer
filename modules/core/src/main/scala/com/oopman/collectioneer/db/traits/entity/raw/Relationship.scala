package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum RelationshipType:
  case ChildOf extends RelationshipType
  case SourceOfPropertiesAndPropertyValues extends RelationshipType
  case SourceOfChildCollections extends RelationshipType

trait Relationship:
  def pk: UUID
  def collectionPK: UUID
  def relatedCollectionPK: UUID
  def relationshipType: RelationshipType
  def index: Int
  def created: ZonedDateTime
  def modified: ZonedDateTime
  
  def rawCopyWith(pk: UUID = pk,
                  collectionPK: UUID = collectionPK,
                  relatedCollectionPK: UUID = relatedCollectionPK,
                  index: Int = index,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): Relationship

trait HasTopLevelCollectionPKAndLevel(val topLevelCollectionPK: UUID, val level: Int)
