package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.cli.CLIConfig
import com.oopman.collectioneer.db.traits
import distage.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

object Properties:
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
    datasourceUri: Option[String],
    count: Int,
    propertyNamesAndUUIDs: Seq[String]
  )

  case class ListPropertiesVerboseResult
  (
    datasourceUri: Option[String],
    count: Int,
    properties: Seq[traits.entity.raw.Property]
  )

  private def listProperties(config: CLIConfig)(propertyDAO: traits.dao.raw.PropertyDAO) = propertyDAO.getAll

  private def listPropertiesByPropertyValueQueries(config: CLIConfig)(propertyDAO: traits.dao.raw.PropertyDAO) =
    propertyDAO.getAllMatchingPropertyValues(Common.generatePropertyValueComparisons(propertyDAO, config.propertyValueQueries))

  def listPropertiesAction(config: CLIConfig): Json =
    // TODO: Use Projected PropertyDAO for verbose results
    val properties = config.propertyValueQueries match
      case Some(_) => Injection.produceRun()(listPropertiesByPropertyValueQueries(config))
      case None => Injection.produceRun()(listProperties(config))
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
