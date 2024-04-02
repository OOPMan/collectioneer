package com.oopman.collectioneer.db.traits.entity.raw

import java.sql.{Blob, Clob}
import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID

sealed trait PropertyValue[T]:
  val pk: UUID
  val collectionPK: UUID
  val propertyPK: UUID
  val index: Int
  val created: ZonedDateTime
  val modified: ZonedDateTime
  val propertyValue: T

trait PropertyValueText extends PropertyValue[String]
trait PropertyValueBytes extends PropertyValue[Array[Byte]]
trait PropertyValueSmallint extends PropertyValue[Short]
trait PropertyValueInt extends PropertyValue[Int]
trait PropertyValueBigint extends PropertyValue[BigInt]
trait PropertyValueBigDecimal extends PropertyValue[BigDecimal]
trait PropertyValueFloat extends PropertyValue[Float]
trait PropertyValueDouble extends PropertyValue[Double]
trait PropertyValueBoolean extends PropertyValue[Boolean]
trait PropertyValueDate extends PropertyValue[LocalDate]
trait PropertyValueTime extends PropertyValue[OffsetTime]
trait PropertyValueTimestamp extends PropertyValue[ZonedDateTime]
trait PropertyValueUUID extends PropertyValue[UUID]
trait PropertyValueJSON extends PropertyValue[io.circe.Json]