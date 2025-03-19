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