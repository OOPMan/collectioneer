package com.oopman.collectioneer.db

import com.oopman.collectioneer.db.traits.entity.raw.Property

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID


object PropertyValueQueryDSL:

  sealed trait Operator

  enum ScalarOperator extends Operator:
    case greaterThan extends ScalarOperator
    case greaterThanOrEqualTo extends ScalarOperator
    case equalTo extends ScalarOperator
    case notEqualTo extends ScalarOperator
    case lessThanOrEualTo extends ScalarOperator
    case lessThan extends ScalarOperator
    case like extends ScalarOperator

  enum VectorOperator extends Operator:
    case greaterThanAny extends VectorOperator
    case greaterThanOrEqualToAny extends VectorOperator
    case equalToAny extends VectorOperator
    case notEqualToAny extends VectorOperator
    case lessThanOrEualToAny extends VectorOperator
    case lessThanAny extends VectorOperator
    case likeAny extends VectorOperator
    case greaterThanAll extends VectorOperator
    case greaterThanOrEqualToAll extends VectorOperator
    case equalToAll extends VectorOperator
    case notEqualToAll extends VectorOperator
    case lessThanOrEualToAll extends VectorOperator
    case lessThanAll extends VectorOperator
    case likeAll extends VectorOperator

  enum LogicalOperator extends Operator:
    case and extends LogicalOperator
    case or extends LogicalOperator

  type ScalarValues =
    BigInt | Boolean | Array[Byte] | LocalDate | Double | Float | Short | Int | io.circe.Json | BigDecimal | String |
    LocalTime | ZonedDateTime | UUID
  type VectorValues =
    Seq[BigInt] | Seq[Boolean] | Seq[Array[Byte]] | Seq[LocalDate] | Seq[Double] | Seq[Float] | Seq[Short] | Seq[Int] |
    Seq[io.circe.Json] | Seq[BigDecimal] | Seq[String] | Seq[LocalTime] | Seq[ZonedDateTime] | Seq[UUID]
  type Values = ScalarValues | VectorValues
  type Operand = Property | Values | Comparison

  sealed trait Comparison:
    val lhs: Operand
    val operator: Operator
    val rhs: Operand

  case class PropertyValueScalarComparison(lhs: Property, operator: ScalarOperator, rhs: ScalarValues) extends Comparison
  case class PropertyValueVectorComparison(lhs: Property, operator: VectorOperator, rhs: VectorValues) extends Comparison
  case class PropertyPropertyComparison(lhs: Property, operator: ScalarOperator, rhs: Property) extends Comparison
  case class NestedComparison(lhs: Comparison, operator: LogicalOperator, rhs: Comparison) extends Comparison

  extension (lhs: Property)
    infix def gt(rhs: ScalarValues) = PropertyValueScalarComparison(lhs, ScalarOperator.greaterThan, rhs)
    infix def gte(rhs: ScalarValues) = PropertyValueScalarComparison(lhs, ScalarOperator.greaterThanOrEqualTo, rhs)
    infix def equalTo(rhs: ScalarValues) = PropertyValueScalarComparison(lhs, ScalarOperator.equalTo, rhs)
    infix def notEqualTo(rhs: ScalarValues) = PropertyValueScalarComparison(lhs, ScalarOperator.notEqualTo, rhs)
    infix def lte(rhs: ScalarValues) = PropertyValueScalarComparison(lhs, ScalarOperator.lessThanOrEualTo, rhs)
    infix def lt(rhs: ScalarValues) = PropertyValueScalarComparison(lhs, ScalarOperator.lessThan, rhs)
    infix def like(rhs: ScalarValues) = PropertyValueScalarComparison(lhs, ScalarOperator.like, rhs)

  extension (lhs: Property)
    infix def gtAny(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.greaterThanAny, rhs)
    infix def gteAny(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.greaterThanOrEqualToAny, rhs)
    infix def equalToAny(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.equalToAny, rhs)
    infix def notEqualToAny(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.notEqualToAny, rhs)
    infix def lteAny(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.lessThanOrEualToAny, rhs)
    infix def ltAny(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.lessThanAny, rhs)
    infix def likeAny(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.likeAny, rhs)
    infix def gtAll(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.greaterThanAll, rhs)
    infix def gteAll(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.greaterThanOrEqualToAll, rhs)
    infix def equalToAll(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.equalToAll, rhs)
    infix def notEqualToAll(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.notEqualToAll, rhs)
    infix def lteAll(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.lessThanOrEualToAll, rhs)
    infix def ltAll(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.lessThanAll, rhs)
    infix def likeAll(rhs: VectorValues) = PropertyValueVectorComparison(lhs, VectorOperator.likeAll, rhs)

  extension (lhs: Property)
      infix def gt(rhs: Property) = PropertyPropertyComparison(lhs, ScalarOperator.greaterThan, rhs)
      infix def gte(rhs: Property) = PropertyPropertyComparison(lhs, ScalarOperator.greaterThanOrEqualTo, rhs)
      infix def equalTo(rhs: Property) = PropertyPropertyComparison(lhs, ScalarOperator.equalTo, rhs)
      infix def notEqualTo(rhs: Property) = PropertyPropertyComparison(lhs, ScalarOperator.notEqualTo, rhs)
      infix def lte(rhs: Property) = PropertyPropertyComparison(lhs, ScalarOperator.lessThanOrEualTo, rhs)
      infix def lt(rhs: Property) = PropertyPropertyComparison(lhs, ScalarOperator.lessThan, rhs)
      infix def like(rhs: Property) = PropertyPropertyComparison(lhs, ScalarOperator.like, rhs)

  extension (lhs: Comparison)
    infix def and(rhs: Comparison) = NestedComparison(lhs, LogicalOperator.and, rhs)
    infix def or(rhs: Comparison) = NestedComparison(lhs, LogicalOperator.or, rhs)
