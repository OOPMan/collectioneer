package com.oopman.collectioneer.db.entity.raw.placeholders

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

object Relationship extends raw.Relationship:
  private def reject: Nothing = throw new RuntimeException("Placeholder Relationship needs to be replaced")
  override def pk: UUID = reject
  override def collectionPK: UUID = reject
  override def relatedCollectionPK: UUID = reject
  override def relationshipType: raw.RelationshipType = reject
  override def index: Int = reject
  override def created: ZonedDateTime = reject
  override def modified: ZonedDateTime = reject
  override def rawCopyWith(pk: UUID, collectionPK: UUID, relatedCollectionPK: UUID, index: Int, created: ZonedDateTime, modified: ZonedDateTime): raw.Relationship = reject

