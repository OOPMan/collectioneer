package com.oopman.collectioneer.db.entity.projected.placeholders

import com.oopman.collectioneer.db.traits.entity.projected
import io.circe.Json

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

object PropertyValue extends projected.PropertyValue:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValue needs to be replaced")
  def property: projected.Property = reject
  def collection: projected.Collection = reject
  def textValues: Seq[String] = reject
  def byteValues: Seq[Array[Byte]] = reject
  def smallintValues: Seq[Short] = reject
  def intValues: Seq[Int] = reject
  def bigintValues: Seq[BigInt] = reject
  def numericValues: Seq[BigDecimal] = reject
  def floatValues: Seq[Float] = reject
  def doubleValues: Seq[Double] = reject
  def booleanValues: Seq[Boolean] = reject
  def dateValues: Seq[LocalDate] = reject
  def timeValues: Seq[LocalTime] = reject
  def timestampValues: Seq[ZonedDateTime] = reject
  def uuidValues: Seq[UUID] = reject
  def jsonValues: Seq[Json] = reject
  def projectedCopyWith(property: projected.Property, collection: projected.Collection, textValues: Seq[String], byteValues: Seq[Array[Byte]],
                        smallintValues: Seq[Short], intValues: Seq[Int], bigintValues: Seq[BigInt], numericValues: Seq[BigDecimal],
                        floatValues: Seq[Float], doubleValues: Seq[Double], booleanValues: Seq[Boolean], dateValues: Seq[LocalDate],
                        timeValues: Seq[LocalTime], timestampValues: Seq[ZonedDateTime], uuidValues: Seq[UUID], jsonValues: Seq[Json]): projected.PropertyValue = reject
