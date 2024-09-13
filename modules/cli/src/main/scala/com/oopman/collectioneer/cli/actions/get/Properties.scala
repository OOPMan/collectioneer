package com.oopman.collectioneer.cli.actions.get

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.db.{Injection, traits}
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
    propertyTypes: List[String],
    deleted: Boolean,
    created: ZonedDateTime,
    modified: ZonedDateTime,
    properties: Map[String, List[String]]
  )
  case class GetPropertiesResult
  (
    dataSourceUri: String,
    count: Int,
    properties: List[PropertyWithPropertyValues]
  )

  def getProperties(config: Config): Json =
    def getProperties(propertyDAO: traits.dao.projected.PropertyDAO) =
      val properties = propertyDAO.getAllMatchingPKs(config.uuids)
      GetPropertiesResult(
        dataSourceUri = config.datasourceUri,
        count = properties.size,
        properties = properties.map(property => PropertyWithPropertyValues(
          pk = property.pk,
          propertyName = property.propertyName,
          propertyTypes = property.propertyTypes.map(_.toString),
          deleted = property.deleted,
          created = property.created, 
          modified = property.modified, 
          properties = Map.from(property.propertyValues.map(Common.propertyValuesToMapTuple))
        ))
      ).asJson
    Injection.produceRun(config)(getProperties)


