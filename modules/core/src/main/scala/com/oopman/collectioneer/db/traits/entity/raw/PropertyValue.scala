package com.oopman.collectioneer.db.traits.entity.raw

import java.sql.{Blob, Clob}
import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

sealed trait PropertyValue[T]:
  def pk: UUID
  def collectionPK: UUID
  def propertyPK: UUID
  def index: Int
  def created: ZonedDateTime
  def modified: ZonedDateTime
  def propertyValue: T
  
  def rawCopyWith(pk: UUID = pk,
                  collectionPK: UUID = collectionPK,
                  propertyPK: UUID = propertyPK,
                  index: Int = index,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified,
                  propertyValue: T = propertyValue): PropertyValue[T]

trait PropertyValueText extends PropertyValue[String]
trait PropertyValueBytes extends PropertyValue[Array[Byte]]
trait PropertyValueSmallint extends PropertyValue[Short]
trait PropertyValueInt extends PropertyValue[Int]
trait PropertyValueBigInt extends PropertyValue[BigInt]
trait PropertyValueBigDecimal extends PropertyValue[BigDecimal]
trait PropertyValueFloat extends PropertyValue[Float]
trait PropertyValueDouble extends PropertyValue[Double]
trait PropertyValueBoolean extends PropertyValue[Boolean]
trait PropertyValueDate extends PropertyValue[LocalDate]
trait PropertyValueTime extends PropertyValue[LocalTime]
trait PropertyValueTimestamp extends PropertyValue[ZonedDateTime]
trait PropertyValueUUID extends PropertyValue[UUID]
trait PropertyValueJSON extends PropertyValue[io.circe.Json]