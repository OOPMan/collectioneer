package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.db.PropertyValueQueryDSL.{ScalarOperator, PropertyValueScalarComparison}
import com.oopman.collectioneer.db.traits

import java.util.UUID

object Common:
  def operatorStringToOperator(operator: String): ScalarOperator = operator match
    case ">"  => ScalarOperator.greaterThan
    case ">=" => ScalarOperator.greaterThanOrEqualTo
    case "==" => ScalarOperator.equalTo
    case "!=" => ScalarOperator.notEqualTo
    case "<=" => ScalarOperator.lessThanOrEualTo
    case "<"  => ScalarOperator.lessThan
    case "~=" => ScalarOperator.like

  def generatePropertyValueComparisons(propertyDAO: traits.dao.raw.PropertyDAO, propertyValueQueries: Option[List[String]]): Seq[PropertyValueScalarComparison] =
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
    } yield PropertyValueScalarComparison(property, operator, value)