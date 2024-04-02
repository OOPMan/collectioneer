package com.oopman.collectioneer.db.traits.entity.projected

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID

trait PropertyValue:
  val property: Property
  val collection: Collection
  val textValues: List[String]
  val byteValues: List[Array[Byte]]
  val smallintValues: List[Short]
  val intValues: List[Int]
  val bigintValues: List[BigInt]
  val numericValues: List[BigDecimal]
  val floatValues: List[Float]
  val doubleValues: List[Double]
  val booleanValues: List[Boolean]
  val dateValues: List[LocalDate]
  val timeValues: List[OffsetTime]
  val timestampValues: List[ZonedDateTime]
  val uuidValues: List[UUID]
  val jsonValues: List[io.circe.Json]