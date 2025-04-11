package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

trait RelationshipCollection:
  def relationshipPK: UUID
  def collectionPK: UUID
  def index: Int
  def created: ZonedDateTime
  def modified: ZonedDateTime
  
  def rawCopyWith(relationshipPK: UUID = relationshipPK,
                  collectionPK: UUID = collectionPK,
                  index: Int = index,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): RelationshipCollection
