package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.PropertyValue
import com.oopman.collectioneer.{CoreProperties, given}

object PropertyValues:
  val singleValue = List(
    PropertyValue(
      property = CoreProperties.minValues,
      smallintValues = List(1)
    ),
    PropertyValue(
      property = CoreProperties.maxValues,
      smallintValues = List(1)
    ))
  val invisibleGATCGProperty = List(
    PropertyValue(
      property = isGATCGProperty,
      booleanValues = List(true)
    ),
    PropertyValue(
      property = CoreProperties.visible,
      booleanValues = List(false)
    )
  )
  val visibleGATCGProperty = List(
    PropertyValue(
      property = isGATCGProperty,
      booleanValues = List(true)
    ),
    PropertyValue(
      property = CoreProperties.visible,
      booleanValues = List(true)
    )
  )
