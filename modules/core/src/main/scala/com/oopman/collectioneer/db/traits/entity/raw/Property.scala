package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyType:
  case text extends PropertyType
  case bytes extends PropertyType
  case smallint extends PropertyType
  case int extends PropertyType
  case bigint extends PropertyType
  case numeric extends PropertyType
  case float extends PropertyType
  case double extends PropertyType
  case boolean extends PropertyType
  case date extends PropertyType
  case time extends PropertyType
  case timestamp extends PropertyType
  case uuid extends PropertyType
  case json extends PropertyType


trait Property:
  def pk: UUID
  def propertyName: String
  def propertyTypes: Seq[PropertyType]
  def deleted: Boolean
  def created: ZonedDateTime
  def modified: ZonedDateTime

  def rawCopyWith(pk: UUID = pk,
                  propertyName: String = propertyName,
                  propertyTypes: Seq[PropertyType] = propertyTypes,
                  deleted: Boolean = deleted,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): Property

  override def equals(obj: Any): Boolean = obj match {
    case property: Property => pk.equals(property.pk)
    case _ => false
  }

  def exactlyEquals(obj: Any): Boolean = super.equals(obj)

  override def hashCode(): Int = pk.hashCode()


trait HasProperty:
  def property: Property