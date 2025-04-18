package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.{projected, raw}

import java.time.ZonedDateTime
import java.util.UUID

case class Collection
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  properties: Seq[raw.Property] = Nil,
  relatedProperties: Seq[raw.Property] = Nil,
  propertyValues: Map[raw.Property, projected.PropertyValue] = Map.empty
) extends projected.Collection:

  def rawCopyWith(pk: UUID = pk,
                  virtual: Boolean = virtual,
                  deleted: Boolean = deleted,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): raw.Collection =
    entity.raw.Collection(pk = pk, virtual = virtual, deleted = deleted, created = created, modified = modified)
  
  def projectedCopyWith(pk: UUID = pk,
                        virtual: Boolean = virtual,
                        deleted: Boolean = deleted,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        properties: Seq[raw.Property] = properties,
                        relatedProperties: Seq[raw.Property] = relatedProperties,
                        propertyValues: Map[raw.Property, projected.PropertyValue] = propertyValues): projected.Collection =
    copy(pk = pk, virtual = virtual, deleted = deleted, created = created, modified = modified,
         properties = properties, relatedProperties = relatedProperties, propertyValues = propertyValues)


