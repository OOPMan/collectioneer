package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.HasProperty
import com.oopman.collectioneer.given

private object RulePropertiesUUIDs:
  val dateAdded = "113f6629-486b-4be9-b98c-e187ed3d85f3"
  
object RuleProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum RuleProperties(val property: projected.Property):
  case dateAdded extends RuleProperties(Property(
    pk = RulePropertiesUUIDs.dateAdded,
    propertyName = "Date Added",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty