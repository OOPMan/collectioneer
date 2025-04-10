package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

trait Property extends raw.Property:
  def propertyValues: Map[raw.Property, PropertyValue]
  
  def projectedCopyWith(pk: UUID = pk,
                        propertyName: String = propertyName,
                        propertyTypes: Seq[raw.PropertyType] = propertyTypes,
                        deleted: Boolean = deleted,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        propertyValues: Map[raw.Property, PropertyValue] = propertyValues): Property


object Property:
 
  // TODO: Move this function
  /**
   * Deduplicates a Sequence of Properties using by the UUID. In the case where Properties with the same UUID are present,
   * the one that was most recently modified is preferred
   *
   * @param properties
   * @return
   */
  def deduplicateProperties(properties: Seq[raw.Property]): Seq[raw.Property] =
    properties
      .groupBy(_.pk)
      .map(
        (_, properties) => properties.reduce(
          (propertyA, propertyB) => if propertyA.modified.isAfter(propertyB.modified) then propertyA else propertyB)
      )
      .toSeq


trait HasProperty extends raw.HasProperty:
  override def property: Property

