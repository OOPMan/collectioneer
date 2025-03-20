package com.oopman.collectioneer.db.traits.entity.projected

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

trait PropertyValue:
  val property: Property
  val collection: Collection
  val textValues: Seq[String]
  val byteValues: Seq[Array[Byte]]
  val smallintValues: Seq[Short]
  val intValues: Seq[Int]
  val bigintValues: Seq[BigInt]
  val numericValues: Seq[BigDecimal]
  val floatValues: Seq[Float]
  val doubleValues: Seq[Double]
  val booleanValues: Seq[Boolean]
  val dateValues: Seq[LocalDate]
  val timeValues: Seq[LocalTime]
  val timestampValues: Seq[ZonedDateTime]
  val uuidValues: Seq[UUID]
  val jsonValues: Seq[io.circe.Json]
  
  def projectedCopyWith(property: Property = property,
                        collection: Collection = collection,
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
                       ): PropertyValue