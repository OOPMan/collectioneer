package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.PropertyValueQueryDSL.PropertyValueComparison
import com.oopman.collectioneer.db.{Injection, traits}
import distage.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.util.UUID

implicit val encodePropertyTypes: Encoder[traits.entity.raw.PropertyType] = (a: traits.entity.raw.PropertyType) => Json.fromString(a.toString)
implicit val encodeProperty: Encoder[traits.entity.raw.Property] = (p: traits.entity.raw.Property) =>
  Json.obj(
    ("pk", Json.fromString(p.pk.toString)),
    ("propertyName", Json.fromString(p.propertyName)),
    ("propertyTypes", p.propertyTypes.asJson),
    ("deleted", Json.fromBoolean(p.deleted)),
    ("created", Json.fromString(p.created.toString)),
    ("modified", Json.fromString(p.modified.toString))
  )

def listProperties(config: Config)(propertyDAO: traits.dao.raw.PropertyDAO) = propertyDAO.getAll

def listPropertiesByPropertyValueQueries(config: Config)(propertyDAO: traits.dao.raw.PropertyDAO) =
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
  propertyDAO.getAllMatchingPropertyValues(propertyValueComparisons)


case class ListPropertiesResult
(
  datasourceUri: String,
  count: Int,
  propertyNamesAndUUIDs: List[String]
)

case class ListPropertiesVerboseResult
(
  datasourceUri: String,
  count: Int,
  properties: List[traits.entity.raw.Property]
)

def listPropertiesAction(config: Config) =
  // TODO: Allow PropertyValueQueries when listing Properties
  // TODO: Use Projected PropertyDAO for verbose results
  val properties = config.propertyValueQueries match
    case Some(_) => Injection.produceRun(config)(listPropertiesByPropertyValueQueries(config))
    case None => Injection.produceRun(config)(listProperties(config))
  if config.verbose then
    ListPropertiesVerboseResult(
      datasourceUri = config.datasourceUri,
      count = properties.size,
      properties = properties
    ).asJson
  else
    ListPropertiesResult(
      datasourceUri = config.datasourceUri,
      count = properties.size,
      propertyNamesAndUUIDs = properties.map(property => s"${property.propertyName} (${property.pk})")
    ).asJson
