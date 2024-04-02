package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw
import scalikejdbc.*

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID

abstract class PropertyValueSQLSyntaxSupport[T <: raw.PropertyValue[?]]
(override val tableName: String, override val schemaName: Option[String] = Some("public")) extends SQLSyntaxSupport[T]:

  def apply(pv: ResultName[T])(rs: WrappedResultSet): T

  val syntax1: QuerySQLSyntaxProvider[SQLSyntaxSupport[T], T] = this.syntax(f"${tableName}_1")

case class PropertyValueBigDecimal
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: BigDecimal = BigDecimal.valueOf(Double.MinValue),
) extends raw.PropertyValueBigDecimal

case class PropertyValueBigint
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: BigInt = BigInt.int2bigInt(Int.MinValue),
) extends raw.PropertyValueBigint

case class PropertyValueBoolean
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Boolean = false,
) extends raw.PropertyValueBoolean

case class PropertyValueBytes
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Array[Byte] = Array.empty,
) extends raw.PropertyValueBytes

case class PropertyValueDate
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: LocalDate = LocalDate.now(),
) extends raw.PropertyValueDate

case class PropertyValueDouble
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Double = Double.MinValue,
) extends raw.PropertyValueDouble

case class PropertyValueFloat
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Float = Float.MinValue,
) extends raw.PropertyValueFloat

case class PropertyValueInt
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Int = Int.MinValue,
) extends raw.PropertyValueInt

case class PropertyValueJSON
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: io.circe.Json = io.circe.Json.Null,
) extends raw.PropertyValueJSON

case class PropertyValueSmallint
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Short = Short.MinValue,
) extends raw.PropertyValueSmallint

case class PropertyValueText
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: String = "",
) extends raw.PropertyValueText

case class PropertyValueTime
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: OffsetTime = OffsetTime.now(),
) extends raw.PropertyValueTime

case class PropertyValueTimestamp
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: ZonedDateTime = ZonedDateTime.now(),
) extends raw.PropertyValueTimestamp

case class PropertyValueUUID
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: UUID = UUID.randomUUID(),
) extends raw.PropertyValueUUID

