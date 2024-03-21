package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

trait Property extends traits.entity.raw.Property:
  val propertyValues: List[PropertyValue]

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
    if distinctNestedProperties.isEmpty then distinctProperties else distinctProperties ++ collectProperties(distinctNestedProperties)
