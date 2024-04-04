package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.PropertyValue

object PropertyValues:
  val singleValue = List(
    PropertyValue(
      property = CoreProperties.minValues.property,
      smallintValues = List(1)
    ),
    PropertyValue(
      property = CoreProperties.maxValues.property,
      smallintValues = List(1)
    )
 )