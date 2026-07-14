package com.oopman.collectioneer.db.entity.raw.placeholders

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.OffsetDateTime
import java.util.UUID

object Collection extends raw.Collection:
  private def reject: Nothing = throw new RuntimeException("Placeholder Collection needs to be replaced")
  def pk: UUID = reject
  def virtual: Boolean = reject
  def deleted: Boolean = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  override def rawCopyWith(pk: UUID, virtual: Boolean, deleted: Boolean, created: OffsetDateTime, modified: OffsetDateTime): raw.Collection = reject
