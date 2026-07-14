package com.oopman.collectioneer.db.entity.projected.placeholders

import com.oopman.collectioneer.db.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.{projected, raw}

import java.time.OffsetDateTime
import java.util.UUID

object Collection extends projected.Collection:
  private def reject: Nothing = throw new RuntimeException("Placeholder Collection needs to be replaced")
  def properties: Seq[raw.Property] = reject
  def relatedProperties: Seq[raw.Property] = reject
  def propertyValues: Map[raw.Property, projected.PropertyValue] = reject
  def pk: UUID = reject
  def virtual: Boolean = reject
  def deleted: Boolean = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def rawCopyWith(pk: UUID, virtual: Boolean, deleted: Boolean, created: OffsetDateTime, modified: OffsetDateTime): raw.Collection = reject
  def projectedCopyWith(pk: UUID, virtual: Boolean, deleted: Boolean, created: OffsetDateTime, modified: OffsetDateTime,
                        properties: Seq[raw.Property], relatedProperties: Seq[raw.Property],
                        propertyValues: Map[raw.Property, projected.PropertyValue]): Collection = reject
