package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.dao.raw.PropertyDAO
import com.oopman.collectioneer.db.entity.PropertyType
import com.oopman.collectioneer.db.entity.raw.Property
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

implicit val encodePropertyTypes: Encoder[PropertyType] = (a: PropertyType) => Json.fromString(a.toString)

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
  properties: List[Property]
)

def listProperties(config: Config) =
  val propertyDAO = new PropertyDAO(config.datasourceUri)
  val properties = propertyDAO.getAll
  config.verbose match
    case true => ListPropertiesVerboseResult(
      datasourceUri = config.datasourceUri,
      count = properties.size,
      properties = properties
    ).asJson
    case false => ListPropertiesResult(
      datasourceUri = config.datasourceUri,
      count = properties.size,
      propertyNamesAndUUIDs = properties.map(property => s"${property.propertyName} (${property.pk})")
    ).asJson
