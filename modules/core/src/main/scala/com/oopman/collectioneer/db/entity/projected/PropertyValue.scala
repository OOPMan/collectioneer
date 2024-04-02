package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.{entity, traits}

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID

case class PropertyValue
(
  property: Property = Property(),
  collection: Collection = Collection(),
  textValues: List[String] = Nil,
  byteValues: List[Array[Byte]] = Nil,
  smallintValues: List[Short] = Nil,
  intValues: List[Int] = Nil,
  bigintValues: List[BigInt] = Nil,
  numericValues: List[BigDecimal] = Nil,
  floatValues: List[Float] = Nil,
  doubleValues: List[Double] = Nil,
  booleanValues: List[Boolean] = Nil,
  dateValues: List[LocalDate] = Nil,
  timeValues: List[OffsetTime] = Nil,
  timestampValues: List[ZonedDateTime] = Nil,
  uuidValues: List[UUID] = Nil,
  jsonValues: List[io.circe.Json] = Nil
) extends traits.entity.projected.PropertyValue

