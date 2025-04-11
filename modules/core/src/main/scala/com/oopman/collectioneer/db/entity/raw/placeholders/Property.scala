package com.oopman.collectioneer.db.entity.raw.placeholders

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

object Property extends raw.Property:
  private def reject: Nothing = throw new RuntimeException("Placeholder Property needs to be replaced")
  override def pk: UUID = reject
  override def propertyName: String = reject
  override def propertyTypes: Seq[raw.PropertyType] = reject
  override def deleted: Boolean = reject
  override def created: ZonedDateTime = reject
  override def modified: ZonedDateTime = reject
  override def rawCopyWith(pk: UUID, propertyName: String, propertyTypes: Seq[raw.PropertyType], deleted: Boolean, created: ZonedDateTime, modified: ZonedDateTime): raw.Property = reject

