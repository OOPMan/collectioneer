package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.HasProperty
import com.oopman.collectioneer.given

private object SetPropertiesUUIDs:
  val prefix = "c5e5a0c3-6c99-4d82-8c82-9da6c127e4e7"
  val language = "69d415ec-4862-47ae-81bf-712411459138"

object SetProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum SetProperties(val property: projected.Property):
  case prefix extends SetProperties(Property(
    pk = SetPropertiesUUIDs.prefix,
    propertyName = "Set Prefix",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case language extends SetProperties(Property(
    pk = SetPropertiesUUIDs.language,
    propertyName = "Set Language",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty