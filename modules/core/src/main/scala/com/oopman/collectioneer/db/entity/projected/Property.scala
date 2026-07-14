package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.{projected, raw}

import java.time.OffsetDateTime
import java.util.UUID

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String = "",
  propertyTypes: Seq[raw.PropertyType] = Nil,
  deleted: Boolean = false,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
  propertyValues: Map[raw.Property, projected.PropertyValue] = Map.empty,
) extends projected.Property:

  def rawCopyWith(pk: UUID = pk,
                  propertyName: String = propertyName,
                  propertyTypes: Seq[raw.PropertyType] = propertyTypes,
                  deleted: Boolean = deleted,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): raw.Property =
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
                        created: OffsetDateTime = created,
                        modified: OffsetDateTime = modified,
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
