package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.{Injection, traits}
import distage.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

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

def listProperties(config: Config) =
  def listProperties(propertyDAO: traits.dao.raw.PropertyDAO) =
    val properties = propertyDAO.getAll
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
  Injection.produceRun(config)(listProperties)
