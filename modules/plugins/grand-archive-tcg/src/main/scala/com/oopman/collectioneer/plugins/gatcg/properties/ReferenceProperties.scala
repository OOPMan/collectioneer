package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.HasProperty
import com.oopman.collectioneer.given

private object ReferencePropertiesUUIDs:
  val kind = "c5798027-af58-4e1d-9f9d-8d08cef67247"
  val slug = "f4945f34-5866-4b9b-a383-f04640309c40"
  val direction = "f3c0ff2b-aa5c-4437-832a-2ceb2429b904"
  
object ReferenceProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum ReferenceProperties(val property: projected.Property):
  case kind extends ReferenceProperties(Property(
    pk = ReferencePropertiesUUIDs.kind,
    propertyName = "Kind",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case slug extends ReferenceProperties(Property(
    pk = ReferencePropertiesUUIDs.slug,
    propertyName = "Slug",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case direction extends ReferenceProperties(Property(
    pk = ReferencePropertiesUUIDs.direction,
    propertyName = "Direction",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty

