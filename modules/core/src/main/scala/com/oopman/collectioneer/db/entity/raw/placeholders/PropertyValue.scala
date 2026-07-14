package com.oopman.collectioneer.db.entity.raw.placeholders

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.{LocalDate, LocalTime, OffsetDateTime}
import java.util.UUID

object PropertyValueLong extends raw.PropertyValueLong:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueLong needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: Long = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: Long): raw.PropertyValueLong = reject

object PropertyValueBoolean extends raw.PropertyValueBoolean:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueBoolean needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: Boolean = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: Boolean): raw.PropertyValueBoolean = reject

object PropertyValueBytes extends raw.PropertyValueBytes:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueBytes needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: Array[Byte] = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: Array[Byte]): raw.PropertyValueBytes = reject

object PropertyValueLocalDate extends raw.PropertyValueLocalDate:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueDate needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: LocalDate = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: LocalDate): raw.PropertyValueLocalDate = reject

object PropertyValueDouble extends raw.PropertyValueDouble:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueDouble needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: Double = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: Double): raw.PropertyValueDouble = reject

object PropertyValueFloat extends raw.PropertyValueFloat:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueFloat needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: Float = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: Float): raw.PropertyValueFloat = reject

object PropertyValueInt extends raw.PropertyValueInt:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueInt needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: Int = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: Int): raw.PropertyValueInt = reject

object PropertyValueJSON extends raw.PropertyValueJSON:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueJSON needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: io.circe.Json = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: io.circe.Json): raw.PropertyValueJSON = reject

object PropertyValueShort extends raw.PropertyValueShort:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueSmallint needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: Short = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: Short): raw.PropertyValueShort = reject

object PropertyValueString extends raw.PropertyValueString:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueText needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: String = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: String): raw.PropertyValueString = reject

object PropertyValueLocalTime extends raw.PropertyValueLocalTime:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueTime needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: LocalTime = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: LocalTime): raw.PropertyValueLocalTime = reject

object PropertyValueOffsetDateTime extends raw.PropertyValueOffsetDateTime:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueTimestamp needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: OffsetDateTime = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: OffsetDateTime): raw.PropertyValueOffsetDateTime = reject

object PropertyValueUUID extends raw.PropertyValueUUID:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueUUID needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: OffsetDateTime = reject
  def modified: OffsetDateTime = reject
  def propertyValue: UUID = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: OffsetDateTime,
                           modified: OffsetDateTime, propertyValue: UUID): raw.PropertyValueUUID = reject