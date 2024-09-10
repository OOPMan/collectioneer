package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.db.PropertyValueQueryDSL.{Operator, PropertyValueComparison}
import com.oopman.collectioneer.db.traits

import java.util.UUID

object Common:
  def operatorStringToOperator(operator: String): Operator = operator match
    case ">"  => Operator.greaterThan
    case ">=" => Operator.greaterThanOrEqualTo
    case "==" => Operator.equalTo
    case "!=" => Operator.notEqualTo
    case "<=" => Operator.lessThanOrEualTo
    case "<"  => Operator.lessThan
    case "~=" => Operator.like

  def generatePropertyValueComparisons(propertyDAO: traits.dao.raw.PropertyDAO, propertyValueQueries: Option[List[String]]) =
    val propertyValueQueryPattern = "^([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})(>|>=|==|!=|<|<=|~=)(\\S+)$".r
    val propertyValueQueriesStrings = propertyValueQueries.getOrElse(Nil)
    val matches = propertyValueQueriesStrings.flatMap(propertyValueQuery => propertyValueQueryPattern.findFirstMatchIn(propertyValueQuery))
    val propertyValueQueryStringComponents = Map.from(
      matches.map(matchResult => (UUID.fromString(matchResult.group(1)), (operatorStringToOperator(matchResult.group(2)), matchResult.group(3))))
    )
    val propertyPKs = propertyValueQueryStringComponents.keys.toList
    // TODO: Why are we retrieving Properties at all?
    val properties = propertyDAO.getAllMatchingPKs(propertyPKs)
    for {
      property <- properties
      propertyType <- property.propertyTypes // TODO: This will cause issues if there is more than one PropertyType
      (operator, value) <- propertyValueQueryStringComponents.get(property.pk)
    } yield PropertyValueComparison(property, operator, value)