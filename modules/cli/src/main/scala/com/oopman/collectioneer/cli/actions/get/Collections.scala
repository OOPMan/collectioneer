package com.oopman.collectioneer.cli.actions.get

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.cli.CLIConfig
import com.oopman.collectioneer.cli.actions.get.Properties.{PropertyWithPropertyValues, propertyToPropertyWithPropertyValues, propertyValuesToPropertiesMap}
import com.oopman.collectioneer.db.traits
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.time.ZonedDateTime
import java.util.UUID

object Collections:
  case class Property
  (
    pk: UUID,
    propertyName: String,
    propertyTypes: Seq[String],
    deleted: Boolean,
    created: ZonedDateTime,
    modified: ZonedDateTime
  )

  case class CollectionWithPropertyValues
  (
    pk: UUID = UUID.randomUUID(),
    virtual: Boolean = false,
    deleted: Boolean = false,
    created: ZonedDateTime = ZonedDateTime.now(),
    modified: ZonedDateTime = ZonedDateTime.now(),
    properties: Seq[Property] = Nil,
    relatedProperties: Seq[Property] = Nil,
    propertyValues: Map[String, Seq[String]] = Map()
  )

  case class GetCollectionsResult
  (
    dataSourceUri: Option[String],
    count: Int,
    collections: Seq[CollectionWithPropertyValues]
  )

  private def propertyEntityToProperty(property: traits.entity.raw.Property): Property =
    Property(
      pk = property.pk,
      propertyName = property.propertyName,
      propertyTypes = property.propertyTypes.map(_.toString),
      deleted = property.deleted,
      created = property.created,
      modified = property.modified
    )
  
  def getCollections(config: CLIConfig): Json =
    def getCollections(collectionDAO: traits.dao.projected.CollectionDAO) =
      val collections = collectionDAO.getAllMatchingPKs(config.uuids)
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
            properties = collection.properties.map(propertyEntityToProperty),
            relatedProperties = collection.properties.map(propertyEntityToProperty),
            propertyValues = propertyValuesToPropertiesMap(collection.propertyValues)
          ))).asJson
    Injection.produceRun()(getCollections)