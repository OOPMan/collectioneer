package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

trait PropertyValue:
  def textValues: Seq[String]
  def byteValues: Seq[Array[Byte]]
  def smallintValues: Seq[Short]
  def intValues: Seq[Int]
  def bigintValues: Seq[BigInt]
  def numericValues: Seq[BigDecimal]
  def floatValues: Seq[Float]
  def doubleValues: Seq[Double]
  def booleanValues: Seq[Boolean]
  def dateValues: Seq[LocalDate]
  def timeValues: Seq[LocalTime]
  def timestampValues: Seq[ZonedDateTime]
  def uuidValues: Seq[UUID]
  def jsonValues: Seq[io.circe.Json]
  
  def projectedCopyWith(textValues: Seq[String] = textValues,
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