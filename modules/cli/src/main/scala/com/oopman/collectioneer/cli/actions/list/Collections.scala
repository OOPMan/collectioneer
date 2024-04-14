package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.PropertyValueQueryDSL.{Operator, PropertyValueComparison, Value}
import com.oopman.collectioneer.db.traits.dao.raw.{CollectionDAO, PropertyDAO}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.{Injection, traits}
import distage.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.{HexFormat, UUID}

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

def stringToValue(value: String, propertyType: PropertyType): Value = propertyType match
  case PropertyType.text => value
  case PropertyType.bytes => value
  case PropertyType.smallint => value.toInt
  case PropertyType.int => value.toInt
  case PropertyType.bigint => BigInt(value)
  case PropertyType.numeric => BigDecimal(value)
  case PropertyType.float => value.toFloat
  case PropertyType.double => value.toDouble
  case PropertyType.boolean => value.toBoolean
  case PropertyType.date => LocalDate.parse(value)
  case PropertyType.time => OffsetTime.parse(value)
  case PropertyType.timestamp => ZonedDateTime.parse(value)
  case PropertyType.uuid => UUID.fromString(value)
  case PropertyType.json => decode[io.circe.Json](value).getOrElse(io.circe.Json.fromString("{}"))

def listCollectionsByPropertyValueQueries(config: Config)(propertyDAO: PropertyDAO, collectionDAO: CollectionDAO) =
  val propertyValueQueryPattern = "^([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})(>|>=|==|!=|<|<=|~=)(\\S+)$".r
  val propertyValueQueriesStrings = config.propertyValueQueries.getOrElse(Nil)
  val matches = propertyValueQueriesStrings.flatMap(propertyValueQuery => propertyValueQueryPattern.findFirstMatchIn(propertyValueQuery))
  val propertyValueQueryStringComponents = Map.from(matches.map(matchResult => (UUID.fromString(matchResult.group(1)), (operatorToOperator(matchResult.group(2)), matchResult.group(3)))))
  val propertyPKs = propertyValueQueryStringComponents.keys.toList
  val properties = propertyDAO.getAllMatchingPKs(propertyPKs)
  val propertyValueComparisons = for {
    property <- properties
    propertyType <- property.propertyTypes
    (operator, value) <- propertyValueQueryStringComponents.get(property.pk)
  } yield PropertyValueComparison(property, operator, stringToValue(value, propertyType))
  collectionDAO.getAllMatchingPropertyValues(propertyValueComparisons: _*)


def listCollectionsAction(config: Config) =
  val collections = config.propertyValueQueries match
    case Some(_) => Injection.produceRun(config)(listCollectionsByPropertyValueQueries(config))
    case None => Injection.produceRun(config)(listCollections(config))
  if config.verbose then
    ListCollectionsVerboseResult(
      dataSourceUri = config.datasourceUri,
      count = collections.size,
      collections = collections
    ).asJson
  else
    ListCollectionsResult(
      dataSourceUri = config.datasourceUri,
      count = collections.size,
      uuids = collections.map(_.pk)
    ).asJson

case class CollectionWithPropertyValues
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  properties: Map[String, List[String]]
)

case class GetCollectionsResult
(
  dataSourceUri: String,
  count: Int,
  collections: List[CollectionWithPropertyValues]
)

def propertyValuesToMapTuple(propertyValue: traits.entity.projected.PropertyValue): (String, List[String]) =
  val hexFormat = HexFormat.of()
  propertyValue.property.propertyName -> (
    propertyValue.textValues ++
    propertyValue.byteValues.map(hexFormat.formatHex) ++
    propertyValue.smallintValues.map(_.toString) ++
    propertyValue.intValues.map(_.toString) ++
    propertyValue.bigintValues.map(_.toString()) ++
    propertyValue.numericValues.map(_.toString()) ++
    propertyValue.floatValues.map(_.toString) ++
    propertyValue.doubleValues.map(_.toString) ++
    propertyValue.booleanValues.map(_.toString) ++
    propertyValue.dateValues.map(_.toString) ++
    propertyValue.timeValues.map(_.toString) ++
    propertyValue.timestampValues.map(_.toString) ++
    propertyValue.uuidValues.map(_.toString) ++
    propertyValue.jsonValues.map(_.spaces2)
  )


def getCollections(config: Config): Json =
  def getCollections(collectionDAO: traits.dao.raw.CollectionDAO, propertyValueDAO: traits.dao.projected.PropertyValueDAO) =
    val collections = collectionDAO.getAllMatchingPKs(config.uuids)
    // TODO: Retrieve PropertyValueSets associated with Collection
    val propertyValues = propertyValueDAO.getPropertyValuesByCollectionUUIDs(config.uuids)
    val propertyValuesByPVSUUID = propertyValues.groupBy(_.collection.pk)
    GetCollectionsResult(
      dataSourceUri = config.datasourceUri,
      count = collections.size,
      collections = collections
        .map(collection => CollectionWithPropertyValues(
          pk = collection.pk,
          virtual = collection.virtual,
          deleted = collection.deleted,
          created = collection.created,
          modified = collection.modified,
          properties = Map.from(propertyValuesByPVSUUID
            .getOrElse(collection.pk, Nil)
            .map(propertyValuesToMapTuple))
        ))).asJson
  Injection.produceRun(config)(getCollections)
