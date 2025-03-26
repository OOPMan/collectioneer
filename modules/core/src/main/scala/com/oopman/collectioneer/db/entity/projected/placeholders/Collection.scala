package com.oopman.collectioneer.db.entity.projected.placeholders

import com.oopman.collectioneer.db.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.{projected, raw}

import java.time.ZonedDateTime
import java.util.UUID

object Collection extends projected.Collection:
  private def reject: Nothing = throw new RuntimeException("Placeholder Collection needs to be replaced")
  def properties: Seq[projected.Property] = reject
  def relatedProperties: Seq[projected.Property] = reject
  def propertyValues: Map[projected.Property, projected.PropertyValue] = reject
  def pk: UUID = reject
  def virtual: Boolean = reject
  def deleted: Boolean = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def rawCopyWith(pk: UUID, virtual: Boolean, deleted: Boolean, created: ZonedDateTime, modified: ZonedDateTime): raw.Collection = reject
  def projectedCopyWith(pk: UUID, virtual: Boolean, deleted: Boolean, created: ZonedDateTime, modified: ZonedDateTime,
                        properties: Seq[projected.Property], relatedProperties: Seq[projected.Property],
                        propertyValues: Map[projected.Property, projected.PropertyValue]): Collection = reject
