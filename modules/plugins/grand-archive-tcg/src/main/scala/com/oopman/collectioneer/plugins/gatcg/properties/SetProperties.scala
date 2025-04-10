package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.given

private object SetPropertiesUUIDs:
  val setName = "542a005a-6bfd-4fe6-8d5d-6a29573bfdc4"
  val setPrefix = "c5e5a0c3-6c99-4d82-8c82-9da6c127e4e7"
  val setLanguage = "69d415ec-4862-47ae-81bf-712411459138"

object SetProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum SetProperties(val property: projected.Property):
  case setName extends SetProperties(Property(
    pk = SetPropertiesUUIDs.setName,
    propertyName = "Set Name",
    propertyTypes = List(PropertyType.text),
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case setPrefix extends SetProperties(Property(
    pk = SetPropertiesUUIDs.setPrefix,
    propertyName = "Set Prefix",
    propertyTypes = List(PropertyType.text),
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case setLanguage extends SetProperties(Property(
    pk = SetPropertiesUUIDs.setLanguage,
    propertyName = "Set Language",
    propertyTypes = List(PropertyType.text),
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty