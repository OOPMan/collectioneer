package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType

import java.time.ZonedDateTime
import java.util.UUID

trait Property extends traits.entity.raw.Property:
  def propertyValues: Seq[PropertyValue]
  
  def projectedCopyWith(pk: UUID = pk,
                        propertyName: String = propertyName,
                        propertyTypes: Seq[PropertyType] = propertyTypes,
                        deleted: Boolean = deleted,
                        created: ZonedDateTime = created,
                        modified: ZonedDateTime = modified,
                        propertyValues: Seq[PropertyValue] = propertyValues): Property

object Property:
  /**
   * Deduplicates a Sequence of Properties using by the UUID. In the case where Properties with the same UUID are present,
   * the one that was most recently modified is preferred
   *
   * @param properties
   * @return
   */
  def deduplicateProperties(properties: Seq[Property]): Seq[Property] =
    properties
      .groupBy(_.pk)
      .map(
        (_, properties) => properties.reduce(
          (propertyA, propertyB) => if propertyA.modified.isAfter(propertyB.modified) then propertyA else propertyB)
      )
      .toSeq

  /**
   * Collects all distinct (by UUID) Properties in the sequence, including those of PropertyValues, Properties on
   * those PropertyValues and so on and so forth
   *
   * @param properties
   * @return
   */
  def collectProperties(properties: Seq[Property]): Seq[Property] =
    val distinctProperties = deduplicateProperties(properties)
    val distinctPropertiesMap = distinctProperties.groupBy(_.pk)
    val distinctNestedProperties = deduplicateProperties(properties.flatMap(_.propertyValues.map(_.property))).filterNot(p => distinctPropertiesMap.contains(p.pk))
    // TODO: This is potentially flawed as it will not detect property duplication among grand-children
    if distinctNestedProperties.isEmpty then distinctProperties else distinctProperties ++ collectProperties(distinctNestedProperties)
