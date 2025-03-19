package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.{entity, traits}

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

case class PropertyValue
(
  property: Property = Property(),
  collection: Collection = Collection(),
  textValues: Seq[String] = Nil,
  byteValues: Seq[Array[Byte]] = Nil,
  smallintValues: Seq[Short] = Nil,
  intValues: Seq[Int] = Nil,
  bigintValues: Seq[BigInt] = Nil,
  numericValues: Seq[BigDecimal] = Nil,
  floatValues: Seq[Float] = Nil,
  doubleValues: Seq[Double] = Nil,
  booleanValues: Seq[Boolean] = Nil,
  dateValues: Seq[LocalDate] = Nil,
  timeValues: Seq[LocalTime] = Nil,
  timestampValues: Seq[ZonedDateTime] = Nil,
  uuidValues: Seq[UUID] = Nil,
  jsonValues: Seq[io.circe.Json] = Nil
) extends traits.entity.projected.PropertyValue

