package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.PropertyValue

object PropertyValues:
  val singleValue = List(
    PropertyValue(
      property = CoreProperties.minValues.property,
      tinyintValues = List(1)
    ),
    PropertyValue(
      property = CoreProperties.maxValues.property,
      tinyintValues = List(1)
    )
 )