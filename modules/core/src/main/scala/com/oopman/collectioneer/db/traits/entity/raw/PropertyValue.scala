package com.oopman.collectioneer.db.traits.entity.raw

import java.time.{LocalDate, LocalTime, OffsetDateTime}
import java.util.UUID

sealed trait PropertyValue[T]:
  def pk: UUID
  def collectionPK: UUID
  def propertyPK: UUID
  def index: Int
  def created: OffsetDateTime
  def modified: OffsetDateTime
  def propertyValue: T
  
  def rawCopyWith(pk: UUID = pk,
                  collectionPK: UUID = collectionPK,
                  propertyPK: UUID = propertyPK,
                  index: Int = index,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified,
                  propertyValue: T = propertyValue): PropertyValue[T]

trait PropertyValueString extends PropertyValue[String]
trait PropertyValueBytes extends PropertyValue[Array[Byte]]
trait PropertyValueShort extends PropertyValue[Short]
trait PropertyValueInt extends PropertyValue[Int]
trait PropertyValueLong extends PropertyValue[Long]
trait PropertyValueFloat extends PropertyValue[Float]
trait PropertyValueDouble extends PropertyValue[Double]
trait PropertyValueBoolean extends PropertyValue[Boolean]
trait PropertyValueLocalDate extends PropertyValue[LocalDate]
trait PropertyValueLocalTime extends PropertyValue[LocalTime]
trait PropertyValueOffsetDateTime extends PropertyValue[OffsetDateTime]
trait PropertyValueUUID extends PropertyValue[UUID]
trait PropertyValueJSON extends PropertyValue[io.circe.Json]