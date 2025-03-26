package com.oopman.collectioneer.db.entity.raw.placeholders

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

object PropertyValueBigDecimal extends raw.PropertyValueBigDecimal:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueBigDecimal needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: BigDecimal = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: BigDecimal): raw.PropertyValueBigDecimal = reject

object PropertyValueBigInt extends raw.PropertyValueBigInt:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueBigInt needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: BigInt = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: BigInt): raw.PropertyValueBigInt = reject

object PropertyValueBoolean extends raw.PropertyValueBoolean:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueBoolean needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: Boolean = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: Boolean): raw.PropertyValueBoolean = reject

object PropertyValueBytes extends raw.PropertyValueBytes:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueBytes needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: Array[Byte] = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: Array[Byte]): raw.PropertyValueBytes = reject

object PropertyValueDate extends raw.PropertyValueDate:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueDate needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: LocalDate = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: LocalDate): raw.PropertyValueDate = reject

object PropertyValueDouble extends raw.PropertyValueDouble:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueDouble needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: Double = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: Double): raw.PropertyValueDouble = reject

object PropertyValueFloat extends raw.PropertyValueFloat:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueFloat needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: Float = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: Float): raw.PropertyValueFloat = reject

object PropertyValueInt extends raw.PropertyValueInt:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueInt needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: Int = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: Int): raw.PropertyValueInt = reject

object PropertyValueJSON extends raw.PropertyValueJSON:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueJSON needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: io.circe.Json = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: io.circe.Json): raw.PropertyValueJSON = reject

object PropertyValueSmallint extends raw.PropertyValueSmallint:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueSmallint needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: Short = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: Short): raw.PropertyValueSmallint = reject

object PropertyValueText extends raw.PropertyValueText:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueText needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: String = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: String): raw.PropertyValueText = reject

object PropertyValueTime extends raw.PropertyValueTime:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueTime needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: LocalTime = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: LocalTime): raw.PropertyValueTime = reject

object PropertyValueTimestamp extends raw.PropertyValueTimestamp:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueTimestamp needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: ZonedDateTime = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: ZonedDateTime): raw.PropertyValueTimestamp = reject

object PropertyValueUUID extends raw.PropertyValueUUID:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValueUUID needs to be replaced")
  def pk: UUID = reject
  def collectionPK: UUID = reject
  def propertyPK: UUID = reject
  def index: Int = reject
  def created: ZonedDateTime = reject
  def modified: ZonedDateTime = reject
  def propertyValue: UUID = reject
  def rawCopyWith(pk: UUID, collectionPK: UUID, propertyPK: UUID, index: Int, created: ZonedDateTime,
                           modified: ZonedDateTime, propertyValue: UUID): raw.PropertyValueUUID = reject