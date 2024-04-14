package com.oopman.collectioneer.db

import com.oopman.collectioneer.db.traits.entity.raw.Property

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID


object PropertyValueQueryDSL:

  enum Operator:
    case greaterThan extends Operator
    case greaterThanOrEqualTo extends Operator
    case equalTo extends Operator
    case notEqualTo extends Operator
    case lessThanOrEualTo extends Operator
    case lessThan extends Operator
    case like extends Operator

  enum LogicalOperator:
    case and extends LogicalOperator
    case or extends LogicalOperator

  type Value = BigInt | Boolean | Byte | LocalDate | Double | Float | Int | io.circe.Json | BigDecimal | String | OffsetTime | ZonedDateTime | UUID
  type Operand = Property | Value | Seq[Value] | Comparison

  sealed trait Comparison:
    val lhs: Operand
    val operator: Operator | LogicalOperator
    val rhs: Operand

  case class PropertyValueComparison(lhs: Property, operator: Operator, rhs: Value | Seq[Value]) extends Comparison
  case class PropertyPropertyComparison(lhs: Property, operator: Operator, rhs: Property) extends Comparison
  case class NestedComparison(lhs: Comparison, operator: LogicalOperator, rhs: Comparison) extends Comparison

  extension (lhs: Property)
    def gt(rhs: Value | Seq[Value]) = PropertyValueComparison(lhs, Operator.greaterThan, rhs)
    def gte(rhs: Value | Seq[Value]) = PropertyValueComparison(lhs, Operator.greaterThanOrEqualTo, rhs)
    def eq(rhs: Value | Seq[Value]) = PropertyValueComparison(lhs, Operator.equalTo, rhs)
    def notEq(rhs: Value | Seq[Value]) = PropertyValueComparison(lhs, Operator.notEqualTo, rhs)
    def lte(rhs: Value | Seq[Value]) = PropertyValueComparison(lhs, Operator.lessThanOrEualTo, rhs)
    def lt(rhs: Value | Seq[Value]) = PropertyValueComparison(lhs, Operator.lessThan, rhs)
    def like(rhs: Value | Seq[Value]) = PropertyValueComparison(lhs, Operator.like, rhs)

  extension (lhs: Property)
      def gt(rhs: Property) = PropertyPropertyComparison(lhs, Operator.greaterThan, rhs)
      def gte(rhs: Property) = PropertyPropertyComparison(lhs, Operator.greaterThanOrEqualTo, rhs)
      def eq(rhs: Property) = PropertyPropertyComparison(lhs, Operator.equalTo, rhs)
      def notEq(rhs: Property) = PropertyPropertyComparison(lhs, Operator.notEqualTo, rhs)
      def lte(rhs: Property) = PropertyPropertyComparison(lhs, Operator.lessThanOrEualTo, rhs)
      def lt(rhs: Property) = PropertyPropertyComparison(lhs, Operator.lessThan, rhs)
      def like(rhs: Property) = PropertyPropertyComparison(lhs, Operator.like, rhs)

  extension (lhs: Comparison)
    def and(rhs: Comparison) = NestedComparison(lhs, LogicalOperator.and, rhs)
    def or(rhs: Comparison) = NestedComparison(lhs, LogicalOperator.or, rhs)
