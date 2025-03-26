package com.oopman.collectioneer.db.entity.raw.placeholders

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

object Collection extends raw.Collection:
  private def reject: Nothing = throw new RuntimeException("Placeholder Collection needs to be replaced")
  def pk: UUID = reject
  def virtual: Boolean = reject
  def deleted: Boolean = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  override def rawCopyWith(pk: UUID, virtual: Boolean, deleted: Boolean, created: ZonedDateTime, modified: ZonedDateTime): raw.Collection = reject
