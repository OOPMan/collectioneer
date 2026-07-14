package com.oopman.collectioneer.db.traits.entity.raw

import java.time.OffsetDateTime
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
  def created: OffsetDateTime
  def modified: OffsetDateTime
  
  def rawCopyWith(pk: UUID = pk,
                  collectionPK: UUID = collectionPK,
                  relatedCollectionPK: UUID = relatedCollectionPK,
                  index: Int = index,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): Relationship

trait HasTopLevelCollectionPKAndLevel(val topLevelCollectionPK: UUID, val level: Int)
