package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

case class PropertyValueLong
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Long = Long.MinValue,
) extends raw.PropertyValueLong:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: Long): raw.PropertyValueLong =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueBoolean
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Boolean = false,
) extends raw.PropertyValueBoolean:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: Boolean): raw.PropertyValueBoolean =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueBytes
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Array[Byte] = Array.empty,
) extends raw.PropertyValueBytes:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: Array[Byte]): raw.PropertyValueBytes =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueDate
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: LocalDate = LocalDate.now(),
) extends raw.PropertyValueDate:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: LocalDate): raw.PropertyValueDate =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueDouble
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Double = Double.MinValue,
) extends raw.PropertyValueDouble:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: Double): raw.PropertyValueDouble =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueFloat
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Float = Float.MinValue,
) extends raw.PropertyValueFloat:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: Float): raw.PropertyValueFloat =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueInt
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Int = Int.MinValue,
) extends raw.PropertyValueInt:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: Int): raw.PropertyValueInt =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueJSON
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: io.circe.Json = io.circe.Json.Null,
) extends raw.PropertyValueJSON:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: io.circe.Json): raw.PropertyValueJSON =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueShort
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Short = Short.MinValue,
) extends raw.PropertyValueShort:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: Short): raw.PropertyValueShort =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueString
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: String = "",
) extends raw.PropertyValueString:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: String): raw.PropertyValueString =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueTime
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: LocalTime = LocalTime.now(),
) extends raw.PropertyValueTime:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: LocalTime): raw.PropertyValueTime =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueTimestamp
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: ZonedDateTime = ZonedDateTime.now(),
) extends raw.PropertyValueTimestamp:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: ZonedDateTime): raw.PropertyValueTimestamp =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

case class PropertyValueUUID
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: UUID = UUID.randomUUID(),
) extends raw.PropertyValueUUID:

  def rawCopyWith(pk: UUID,
                  collectionPK: UUID,
                  propertyPK: UUID,
                  index: Int,
                  created: ZonedDateTime,
                  modified: ZonedDateTime,
                  propertyValue: UUID): raw.PropertyValueUUID =
    copy(pk = pk, collectionPK = collectionPK, propertyPK = propertyPK, index = index, created = created, modified = modified, propertyValue = propertyValue)

