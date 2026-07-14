package com.oopman.collectioneer.db.entity.projected.placeholders

import com.oopman.collectioneer.db.traits.entity.{projected, raw}
import io.circe.Json

import java.time.{LocalDate, LocalTime, OffsetDateTime}
import java.util.UUID

object PropertyValue extends projected.PropertyValue:
  private def reject: Nothing = throw new RuntimeException("Placeholder PropertyValue needs to be replaced")
  def stringValues: Seq[String] = reject
  def byteValues: Seq[Array[Byte]] = reject
  def shortValues: Seq[Short] = reject
  def intValues: Seq[Int] = reject
  def longValues: Seq[Long] = reject
  def floatValues: Seq[Float] = reject
  def doubleValues: Seq[Double] = reject
  def booleanValues: Seq[Boolean] = reject
  def localDateValues: Seq[LocalDate] = reject
  def localTimeValues: Seq[LocalTime] = reject
  def offsetDateTimeValues: Seq[OffsetDateTime] = reject
  def uuidValues: Seq[UUID] = reject
  def jsonValues: Seq[Json] = reject
  def projectedCopyWith(textValues: Seq[String], byteValues: Seq[Array[Byte]], shortValues: Seq[Short], intValues: Seq[Int],
                        longValues: Seq[Long], floatValues: Seq[Float], doubleValues: Seq[Double], booleanValues: Seq[Boolean], 
                        dateValues: Seq[LocalDate], timeValues: Seq[LocalTime], timestampValues: Seq[OffsetDateTime],
                        uuidValues: Seq[UUID], jsonValues: Seq[Json]): projected.PropertyValue = reject
