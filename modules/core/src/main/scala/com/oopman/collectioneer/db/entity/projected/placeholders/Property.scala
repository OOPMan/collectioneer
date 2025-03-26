package com.oopman.collectioneer.db.entity.projected.placeholders

import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType

import java.time.ZonedDateTime
import java.util.UUID

object Property extends projected.Property:
  private def reject: Nothing = throw new RuntimeException("Placeholder Property needs to be replaced")
  def pk: UUID = reject
  def propertyName: String = reject
  def propertyTypes: Seq[PropertyType] = reject
  def deleted: Boolean = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValues: Seq[projected.PropertyValue] = reject
  def rawCopyWith(pk: UUID, propertyName: String, propertyTypes: Seq[PropertyType], deleted: Boolean,
                           created: ZonedDateTime, modified: ZonedDateTime): projected.Property = reject
  def projectedCopyWith(pk: UUID, propertyName: String, propertyTypes: Seq[PropertyType], deleted: Boolean,
                        created: ZonedDateTime, modified: ZonedDateTime, propertyValues: Seq[projected.PropertyValue]): projected.Property = reject
