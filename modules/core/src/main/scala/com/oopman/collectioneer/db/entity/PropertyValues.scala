package com.oopman.collectioneer.db.entity

import java.sql.{Blob, Clob}
import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

sealed trait PropertyValue

case class PropertyValueVarchars
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: String,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueVarBinaries
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Array[Byte],
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueVarTinyints
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Byte,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueSmallints
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Short,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueInts
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Int,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueBigInts
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Long,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueNumerics
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: BigDecimal,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueFloats
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Float,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueDoubles
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Double,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueBooleans
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Boolean,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueDates
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: LocalDate,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueTimes
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: LocalTime,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueTimestamps
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: ZonedDateTime,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueClobs
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Clob,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueBlobs
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Blob,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueUUIDs
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueJSONs
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: String, // TODO: Replace with a type from a proper JSON library in the future
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue