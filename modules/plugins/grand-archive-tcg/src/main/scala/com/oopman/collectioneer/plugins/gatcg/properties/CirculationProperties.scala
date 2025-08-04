package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.HasProperty
import com.oopman.collectioneer.given

private object CirculationPropertyUUIDs:
  val foil = "b4eed106-7a4a-40d8-8a34-aa02573e4b50"
  val population = "94e6a367-e530-49b5-bef4-27c78923637e"
  val populationOperator = "b68467d6-f1bc-4c56-949d-f8353db19aae"
  val uuid = "c447626e-955b-4d3d-aeb2-47155b9cea02"

object CirculationProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum CirculationProperties(val property: projected.Property):
  case foil extends CirculationProperties(Property(
    pk = CirculationPropertyUUIDs.foil,
    propertyName = "Foil",
    propertyTypes = PropertyType.boolean :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case population extends CirculationProperties(Property(
    pk = CirculationPropertyUUIDs.population,
    propertyName = "Population",
    propertyTypes = PropertyType.int :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty,
  )) with HasProperty
  case populationOperator extends CirculationProperties(Property(
    pk = CirculationPropertyUUIDs.populationOperator,
    propertyName = "Population Operator",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case uuid extends CirculationProperties(Property(
    pk = CirculationPropertyUUIDs.uuid,
    propertyName = "UUID",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty



 
