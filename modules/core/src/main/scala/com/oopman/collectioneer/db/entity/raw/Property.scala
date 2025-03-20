package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw

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
) extends raw.Property:
  
  def rawCopyWith(pk: UUID = pk, 
                  propertyName: String = propertyName, 
                  propertyTypes: Seq[raw.PropertyType] = propertyTypes, 
                  deleted: Boolean = deleted, 
                  created: ZonedDateTime = created, 
                  modified: ZonedDateTime = modified): raw.Property =
    copy(pk = pk, propertyName = propertyName, propertyTypes = propertyTypes, deleted = deleted, created = created, modified = modified)
