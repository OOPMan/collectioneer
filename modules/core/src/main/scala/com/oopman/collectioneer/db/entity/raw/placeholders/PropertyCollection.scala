package com.oopman.collectioneer.db.entity.raw.placeholders

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

object PropertyCollection extends raw.PropertyCollection:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyCollection needs to be replaced")
  override def propertyPK: UUID = reject
  override def collectionPK: UUID = reject
  override def index: Int = reject
  override def propertyCollectionRelationshipType: raw.PropertyCollectionRelationshipType = reject
  override def created: ZonedDateTime = reject
  override def modified: ZonedDateTime = reject
  override def rawCopyWith(propertyPK: UUID, collectionPK: UUID, index: Int, propertyCollectionRelationshipType: raw.PropertyCollectionRelationshipType, 
                           created: ZonedDateTime, modified: ZonedDateTime): raw.PropertyCollection = reject


