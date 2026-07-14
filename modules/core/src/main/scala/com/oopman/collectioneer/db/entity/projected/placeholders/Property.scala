package com.oopman.collectioneer.db.entity.projected.placeholders

import com.oopman.collectioneer.db.traits.entity.{projected, raw}

import java.time.OffsetDateTime
import java.util.UUID

object Property extends projected.Property:
  private def reject: Nothing = throw new RuntimeException("Placeholder Property needs to be replaced")
  def pk: UUID = reject
  def propertyName: String = reject
  def propertyTypes: Seq[raw.PropertyType] = reject
  def deleted: Boolean = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValues: Map[raw.Property, projected.PropertyValue] = reject
  def rawCopyWith(pk: UUID, propertyName: String, propertyTypes: Seq[raw.PropertyType], deleted: Boolean,
                           created: OffsetDateTime, modified: OffsetDateTime): projected.Property = reject
  def projectedCopyWith(pk: UUID, propertyName: String, propertyTypes: Seq[raw.PropertyType], deleted: Boolean,
                        created: OffsetDateTime, modified: OffsetDateTime, propertyValues: Map[raw.Property, projected.PropertyValue]): projected.Property = reject
