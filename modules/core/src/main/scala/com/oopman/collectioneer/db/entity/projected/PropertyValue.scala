package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.projected

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

case class PropertyValue
(
  property: projected.Property = entity.projected.Property(),
  collection: projected.Collection = entity.projected.Collection(),
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
) extends projected.PropertyValue:

  def projectedCopyWith(property: projected.Property = property,
                        collection: projected.Collection = collection,
                        textValues: Seq[String] = textValues,
                        byteValues: Seq[Array[Byte]] = byteValues,
                        smallintValues: Seq[Short] = smallintValues,
                        intValues: Seq[Int] = intValues,
                        bigintValues: Seq[BigInt] = bigintValues,
                        numericValues: Seq[BigDecimal] = numericValues,
                        floatValues: Seq[Float] = floatValues,
                        doubleValues: Seq[Double] = doubleValues,
                        booleanValues: Seq[Boolean] = booleanValues,
                        dateValues: Seq[LocalDate] = dateValues,
                        timeValues: Seq[LocalTime] = timeValues,
                        timestampValues: Seq[ZonedDateTime] = timestampValues,
                        uuidValues: Seq[UUID] = uuidValues,
                        jsonValues: Seq[io.circe.Json] = jsonValues
                       ): projected.PropertyValue =
    copy(
      property = property,
      collection = collection,
      textValues = textValues,
      byteValues = byteValues,
      smallintValues = smallintValues,
      intValues = intValues,
      bigintValues = bigintValues,
      numericValues = numericValues,
      floatValues = floatValues,
      doubleValues = doubleValues,
      booleanValues = booleanValues,
      dateValues = dateValues,
      timeValues = timeValues,
      timestampValues = timestampValues,
      uuidValues = uuidValues,
      jsonValues = jsonValues
    )

