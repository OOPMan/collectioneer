package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.dao.PropertiesDAO
import com.oopman.collectioneer.db.entity.Properties
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

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
  properties: List[Properties]
)

def listProperties(config: Config) =
  val propertiesDAO = new PropertiesDAO(config.datasourceUri)
  val properties = propertiesDAO.getAll()
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
