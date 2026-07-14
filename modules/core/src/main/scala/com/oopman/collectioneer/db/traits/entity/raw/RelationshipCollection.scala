package com.oopman.collectioneer.db.traits.entity.raw

import java.time.OffsetDateTime
import java.util.UUID

trait RelationshipCollection:
  def relationshipPK: UUID
  def collectionPK: UUID
  def index: Int
  def created: OffsetDateTime
  def modified: OffsetDateTime
  
  def rawCopyWith(relationshipPK: UUID = relationshipPK,
                  collectionPK: UUID = collectionPK,
                  index: Int = index,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): RelationshipCollection
