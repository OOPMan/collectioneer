package com.oopman.collectioneer.db.traits.entity.raw

import java.time.OffsetDateTime
import java.util.UUID

enum PropertyType:
  case String extends PropertyType
  case Bytes extends PropertyType
  case Short extends PropertyType
  case Int extends PropertyType
  case Long extends PropertyType
  case Float extends PropertyType
  case Double extends PropertyType
  case Boolean extends PropertyType
  case LocalDate extends PropertyType
  case LocalTime extends PropertyType
  case OffsetDateTime extends PropertyType
  case UUID extends PropertyType
  case JSON extends PropertyType


trait Property:
  def pk: UUID
  def propertyName: String
  def propertyTypes: Seq[PropertyType]
  def deleted: Boolean
  def created: OffsetDateTime
  def modified: OffsetDateTime

  def rawCopyWith(pk: UUID = pk,
                  propertyName: String = propertyName,
                  propertyTypes: Seq[PropertyType] = propertyTypes,
                  deleted: Boolean = deleted,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): Property

  override def equals(obj: Any): Boolean = obj match {
    case property: Property => pk.equals(property.pk)
    case hasProperty: HasProperty => pk.equals(hasProperty.property.pk)
    case _ => false
  }

  def exactlyEquals(obj: Any): Boolean = super.equals(obj)

  override def hashCode(): Int = pk.hashCode()


trait HasProperty:
  def property: Property