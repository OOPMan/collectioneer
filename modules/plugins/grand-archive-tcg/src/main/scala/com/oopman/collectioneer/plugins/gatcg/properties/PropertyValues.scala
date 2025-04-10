package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.traits.entity.{projected, raw}
import com.oopman.collectioneer.db.entity.projected.PropertyValue
import com.oopman.collectioneer.{CoreProperties, given}
import com.oopman.collectioneer.db.traits.entity.raw.given 

object PropertyValues:
  val singleValue: Map[raw.Property, projected.PropertyValue] = Map(
    CoreProperties.minValues -> PropertyValue(smallintValues = List(1)),
    CoreProperties.maxValues -> PropertyValue(smallintValues = List(1)))
  val invisibleGATCGProperty: Map[raw.Property, projected.PropertyValue] = Map(
    isGATCGProperty -> PropertyValue(booleanValues = List(true)),
    CoreProperties.visible -> PropertyValue(booleanValues = List(false))
  )
  val visibleGATCGProperty: Map[raw.Property, projected.PropertyValue] = Map(
    isGATCGProperty -> PropertyValue(booleanValues = List(true)),
    CoreProperties.visible -> PropertyValue(booleanValues = List(true))
  )
