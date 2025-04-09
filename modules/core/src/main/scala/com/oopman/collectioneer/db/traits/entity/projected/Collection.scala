package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits.entity.{raw, projected}

import java.time.ZonedDateTime
import java.util.UUID

trait Collection extends raw.Collection:
  def properties: Seq[raw.Property]
  def relatedProperties: Seq[raw.Property]
  def propertyValues: Map[raw.Property, projected.PropertyValue]
  // TODO: Add fields for relationships
  // def relationships:List[Relationship]
  
  def projectedCopyWith(pk: UUID = pk,
                        virtual: Boolean = virtual,
                        deleted: Boolean = deleted,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        properties: Seq[raw.Property] =  properties,
                        relatedProperties: Seq[raw.Property] = relatedProperties,
                        propertyValues: Map[raw.Property, projected.PropertyValue] = propertyValues): Collection

  override def toString: String = s"Projected Collection ($pk)"
