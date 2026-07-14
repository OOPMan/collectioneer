package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.{LocalDate, LocalTime, OffsetDateTime}
import java.util.UUID

case class PropertyValueLong
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: Long = Long.MinValue,
) extends raw.PropertyValueLong:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: Long): raw.PropertyValueLong =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueBoolean
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: Boolean = false,
) extends raw.PropertyValueBoolean:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: Boolean): raw.PropertyValueBoolean =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueBytes
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: Array[Byte] = Array.empty,
) extends raw.PropertyValueBytes:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: Array[Byte]): raw.PropertyValueBytes =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueLocalDate
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: LocalDate = LocalDate.now(),
) extends raw.PropertyValueLocalDate:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: LocalDate): raw.PropertyValueLocalDate =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueDouble
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: Double = Double.MinValue,
) extends raw.PropertyValueDouble:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: Double): raw.PropertyValueDouble =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueFloat
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: Float = Float.MinValue,
) extends raw.PropertyValueFloat:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: Float): raw.PropertyValueFloat =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueInt
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: Int = Int.MinValue,
) extends raw.PropertyValueInt:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: Int): raw.PropertyValueInt =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueJSON
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: io.circe.Json = io.circe.Json.Null,
) extends raw.PropertyValueJSON:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: io.circe.Json): raw.PropertyValueJSON =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueShort
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: Short = Short.MinValue,
) extends raw.PropertyValueShort:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: Short): raw.PropertyValueShort =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueString
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: String = "",
) extends raw.PropertyValueString:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: String): raw.PropertyValueString =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueLocalTime
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: LocalTime = LocalTime.now(),
) extends raw.PropertyValueLocalTime:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: LocalTime): raw.PropertyValueLocalTime =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueOffsetDateTime
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: OffsetDateTime = OffsetDateTime.now(),
) extends raw.PropertyValueOffsetDateTime:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: OffsetDateTime): raw.PropertyValueOffsetDateTime =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueUUID
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValue: UUID = UUID.randomUUID(),
) extends raw.PropertyValueUUID:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: OffsetDateTime,
                  modified: OffsetDateTime,
                  propertyValue: UUID): raw.PropertyValueUUID =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

