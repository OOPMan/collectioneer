package com.oopman.collectioneer.db.traits.entity.projected

import java.sql.{Blob, Clob}
import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID

trait PropertyValue:
  val property: Property
  val collection: Collection
  val varcharValues: List[String]
  val varbinaryValues: List[Array[Byte]]
  val tinyintValues: List[Byte]
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
  val clobValues: List[Clob]
  val blobValues: List[Blob]
  val uuidValues: List[UUID]
  val jsonValues: List[Array[Byte]]