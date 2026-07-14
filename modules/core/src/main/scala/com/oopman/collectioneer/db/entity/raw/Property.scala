package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw

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
) extends raw.Property:
  
  def rawCopyWith(pk: UUID = pk, 
                  propertyName: String = propertyName, 
                  propertyTypes: Seq[raw.PropertyType] = propertyTypes, 
                  deleted: Boolean = deleted, 
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): raw.Property =
    copy(pk = pk, propertyName = propertyName, propertyTypes = propertyTypes, deleted = deleted, created = created, modified = modified)
