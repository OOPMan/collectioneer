package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.{projected, raw}

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

case class PropertyValue
(
  stringValues: Seq[String] = Nil,
  byteValues: Seq[Array[Byte]] = Nil,
  shortValues: Seq[Short] = Nil,
  intValues: Seq[Int] = Nil,
  longValues: Seq[Long] = Nil, // TODO: We need to re-write this to Long
  floatValues: Seq[Float] = Nil,
  doubleValues: Seq[Double] = Nil,
  booleanValues: Seq[Boolean] = Nil,
  dateValues: Seq[LocalDate] = Nil,
  timeValues: Seq[LocalTime] = Nil,
  timestampValues: Seq[ZonedDateTime] = Nil,
  uuidValues: Seq[UUID] = Nil,
  jsonValues: Seq[io.circe.Json] = Nil
) extends projected.PropertyValue:

  def projectedCopyWith(stringValues: Seq[String] = stringValues,
                        byteValues: Seq[Array[Byte]] = byteValues,
                        shortValues: Seq[Short] = shortValues,
                        intValues: Seq[Int] = intValues,
                        longValues: Seq[Long] = longValues,
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
      stringValues = stringValues,
      byteValues = byteValues,
      shortValues = shortValues,
      intValues = intValues,
      longValues = longValues,
      floatValues = floatValues,
      doubleValues = doubleValues,
      booleanValues = booleanValues,
      dateValues = dateValues,
      timeValues = timeValues,
      timestampValues = timestampValues,
      uuidValues = uuidValues,
      jsonValues = jsonValues
    )
