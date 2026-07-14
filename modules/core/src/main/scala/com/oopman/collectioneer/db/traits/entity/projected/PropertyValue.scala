package com.oopman.collectioneer.db.traits.entity.projected

import java.time.{LocalDate, LocalTime, OffsetDateTime}
import java.util.UUID

trait PropertyValue:
  def stringValues: Seq[String]
  def byteValues: Seq[Array[Byte]]
  def shortValues: Seq[Short]
  def intValues: Seq[Int]
  def longValues: Seq[Long]
  def floatValues: Seq[Float]
  def doubleValues: Seq[Double]
  def booleanValues: Seq[Boolean]
  def localDateValues: Seq[LocalDate]
  def localTimeValues: Seq[LocalTime]
  def offsetDateTimeValues: Seq[OffsetDateTime]
  def uuidValues: Seq[UUID]
  def jsonValues: Seq[io.circe.Json]
  
  def projectedCopyWith(stringValues: Seq[String] = stringValues,
                        byteValues: Seq[Array[Byte]] = byteValues,
                        shortValues: Seq[Short] = shortValues,
                        intValues: Seq[Int] = intValues,
                        longValues: Seq[Long] = longValues,
                        floatValues: Seq[Float] = floatValues,
                        doubleValues: Seq[Double] = doubleValues,
                        booleanValues: Seq[Boolean] = booleanValues,
                        localDateValues: Seq[LocalDate] = localDateValues,
                        localTimeValues: Seq[LocalTime] = localTimeValues,
                        offsetDateTimeValues: Seq[OffsetDateTime] = offsetDateTimeValues,
                        uuidValues: Seq[UUID] = uuidValues,
                        jsonValues: Seq[io.circe.Json] = jsonValues
                       ): PropertyValue