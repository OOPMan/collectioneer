package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

trait Collection extends traits.entity.raw.Collection:
  val properties: Seq[Property]
  val relatedProperties: Seq[Property]
  val propertyValues: Map[Property, PropertyValue]
  // TODO: Add fields for relationships
  // val relationships:List[Relationship]
  
  def projectedCopyWith(pk: UUID = pk,
                        virtual: Boolean = virtual,
                        deleted: Boolean = deleted,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        properties: Seq[Property] =  properties,
                        relatedProperties: Seq[Property] = relatedProperties,
                        propertyValues: Map[Property, PropertyValue] = propertyValues): Collection

  override def toString: String = s"Projected Collection ($pk)"
