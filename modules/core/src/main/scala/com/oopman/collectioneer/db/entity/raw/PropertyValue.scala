package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.entity.PropertyValue

import java.sql.{Blob, Clob}
import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

sealed trait PropertyValue

case class PropertyValueVarchar
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: String,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueVarBinary
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Array[Byte],
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueVarTinyint
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Byte,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueSmallint
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Short,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueInt
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Int,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueBigInt
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Long,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueNumeric
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: BigDecimal,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueFloat
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Float,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueDouble
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Double,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueBoolean
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Boolean,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueDate
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: LocalDate,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueTime
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: LocalTime,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueTimestamp
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: ZonedDateTime,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueClob
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Clob,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueBlob
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: Blob,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueUUID
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue

case class PropertyValueJSON
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  propertyValue: String, // TODO: Replace with a type from a proper JSON library in the future
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends PropertyValue