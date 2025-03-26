package com.oopman.collectioneer.db.entity.raw.placeholders

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

object RelationshipCollection extends raw.RelationshipCollection:
  private def reject: Nothing = throw new RuntimeException("Placeholder RelationshipCollection needs to be replaced")
  override def relationshipPK: UUID = reject
  override def collectionPK: UUID = reject
  override def index: Int = reject
  override def created: ZonedDateTime = reject
  override def modified: ZonedDateTime = reject
  override def rawCopyWith(relationshipPK: UUID, collectionPK: UUID, index: Int, created: ZonedDateTime, modified: ZonedDateTime): raw.RelationshipCollection = reject

