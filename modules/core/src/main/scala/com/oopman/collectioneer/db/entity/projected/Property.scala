package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.{raw, projected}

import java.time.ZonedDateTime
import java.util.UUID

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String = "",
  propertyTypes: Seq[raw.PropertyType] = Nil,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValues: Map[raw.Property, projected.PropertyValue] = Map.empty,
) extends projected.Property:

  def rawCopyWith(pk: UUID = pk,
                  propertyName: String = propertyName,
                  propertyTypes: Seq[raw.PropertyType] = propertyTypes,
                  deleted: Boolean = deleted,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): raw.Property =
    entity.raw.Property(
      pk = pk, 
      propertyName = propertyName, 
      propertyTypes = propertyTypes, 
      deleted = deleted, 
      created = created, 
      modified = modified
    )

  def projectedCopyWith(pk: UUID = pk,
                        propertyName: String = propertyName,
                        propertyTypes: Seq[raw.PropertyType] = propertyTypes,
                        deleted: Boolean = deleted,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        propertyValues: Map[raw.Property, projected.PropertyValue] = propertyValues): projected.Property =
    copy(
      pk = pk, 
      propertyName = propertyName, 
      propertyTypes = propertyTypes, 
      deleted = deleted, 
      created = created, 
      modified = modified, 
      propertyValues = propertyValues
    )
