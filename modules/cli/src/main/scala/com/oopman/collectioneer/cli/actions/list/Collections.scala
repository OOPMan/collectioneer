package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.PropertyValueQueryDSL.{Operator, PropertyValueComparison}
import com.oopman.collectioneer.db.traits.dao.raw.{CollectionDAO, PropertyDAO}
import com.oopman.collectioneer.db.{Injection, traits}
import distage.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.util.UUID

implicit val encodeCollection: Encoder[traits.entity.raw.Collection] = (c: traits.entity.raw.Collection) =>
  Json.obj(
    ("pk", Json.fromString(c.pk.toString)),
    ("virtual", Json.fromBoolean(c.virtual)),
    ("deleted", Json.fromBoolean(c.deleted)),
    ("created", Json.fromString(c.created.toString)),
    ("modified", Json.fromString(c.modified.toString))
  )


case class ListCollectionsResult
(
  dataSourceUri: String,
  count: Int,
  uuids: List[UUID]
)

case class ListCollectionsVerboseResult
(
  dataSourceUri: String,
  count: Int,
  collections: List[traits.entity.raw.Collection]
)

def listCollections(config: Config)(collectionDAO: traits.dao.raw.CollectionDAO) = collectionDAO.getAll

def operatorToOperator(operator: String): Operator = operator match
  case ">"  => Operator.greaterThan
  case ">=" => Operator.greaterThanOrEqualTo
  case "==" => Operator.equalTo
  case "!=" => Operator.notEqualTo
  case "<=" => Operator.lessThanOrEualTo
  case "<"  => Operator.lessThan
  case "~=" => Operator.like

def listCollectionsByPropertyValueQueries(config: Config)(propertyDAO: PropertyDAO, collectionDAO: CollectionDAO) =
  val propertyValueQueryPattern = "^([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})(>|>=|==|!=|<|<=|~=)(\\S+)$".r
  val propertyValueQueriesStrings = config.propertyValueQueries.getOrElse(Nil)
  val matches = propertyValueQueriesStrings.flatMap(propertyValueQuery => propertyValueQueryPattern.findFirstMatchIn(propertyValueQuery))
  val propertyValueQueryStringComponents = Map.from(matches.map(matchResult => (UUID.fromString(matchResult.group(1)), (operatorToOperator(matchResult.group(2)), matchResult.group(3)))))
  val propertyPKs = propertyValueQueryStringComponents.keys.toList
  val properties = propertyDAO.getAllMatchingPKs(propertyPKs)
  val propertyValueComparisons = for {
    property <- properties
    propertyType <- property.propertyTypes // TODO: This will cause issues if there is more than one PropertyType
    (operator, value) <- propertyValueQueryStringComponents.get(property.pk)
  } yield PropertyValueComparison(property, operator, value)
  collectionDAO.getAllRelatedMatchingPropertyValues(propertyValueComparisons)


def listCollectionsAction(config: Config) =
  val collections = config.propertyValueQueries match
    case Some(_) => Injection.produceRun(config)(listCollectionsByPropertyValueQueries(config))
    case None => Injection.produceRun(config)(listCollections(config))
  if config.verbose then
    // TODO: Verbose list includes all property values for each collection
    ListCollectionsVerboseResult(
      dataSourceUri = config.datasourceUri,
      count = collections.size,
      collections = collections
    ).asJson
  else
    // TODO: non-verbose list should include UUID and Property Name
    ListCollectionsResult(
      dataSourceUri = config.datasourceUri,
      count = collections.size,
      uuids = collections.map(_.pk)
    ).asJson
