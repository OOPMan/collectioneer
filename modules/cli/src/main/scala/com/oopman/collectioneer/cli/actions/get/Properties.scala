package com.oopman.collectioneer.cli.actions.get

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.traits
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.time.ZonedDateTime
import java.util.UUID

object Properties:
  case class PropertyWithPropertyValues
  (
    pk: UUID,
    propertyName: String,
    propertyTypes: Seq[String],
    deleted: Boolean,
    created: ZonedDateTime,
    modified: ZonedDateTime,
    properties: Map[String, Seq[String]]
  )
  case class GetPropertiesResult
  (
    dataSourceUri: Option[String],
    count: Int,
    properties: Seq[PropertyWithPropertyValues]
  )

  def propertyValuesToPropertiesMap(propertyValues: Map[traits.entity.raw.Property, traits.entity.projected.PropertyValue]): Map[String, Seq[String]] =
    for (property, propertyValue) <- propertyValues
    yield property.propertyName -> Common.propertyValueToSeqOfStrings(propertyValue)
  
  def propertyToPropertyWithPropertyValues(property: traits.entity.projected.Property): PropertyWithPropertyValues =
    PropertyWithPropertyValues(
      pk = property.pk,
      propertyName = property.propertyName,
      propertyTypes = property.propertyTypes.map(_.toString),
      deleted = property.deleted,
      created = property.created,
      modified = property.modified,
      properties = propertyValuesToPropertiesMap(property.propertyValues)
    )
    
  def getProperties(config: Config): Json =
    def getProperties(propertyDAO: traits.dao.projected.PropertyDAO) =
      val properties = propertyDAO.getAllMatchingPKs(config.uuids)
      GetPropertiesResult(
        dataSourceUri = config.datasourceUri,
        count = properties.size,
        properties = properties.map(propertyToPropertyWithPropertyValues)
      ).asJson
    Injection.produceRun(Some(config))(getProperties)



