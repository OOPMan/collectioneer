package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.given
import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType


enum GATCGProperties(val property: Property):
  case reserveCost extends GATCGProperties(Property(
    pk = "78237d1d-8be0-4426-8111-d8ef4274f441",
    propertyName = "Reserve Cost",
    propertyTypes = List(PropertyType.tinyint),
    propertyValues = List(
      PropertyValue(
        property = CoreProperties.minValues.property,
        tinyintValues = List(1)
      ),
      PropertyValue(
        property = CoreProperties.maxValues.property,
        tinyintValues = List(1)
      )
    )
  ))
  case memoryCost extends GATCGProperties(Property(
    pk = "39e6999a-17bc-4f8e-bbb9-5ac8e7c91fae",
    propertyName = "Memory Cost",
    propertyTypes = List(PropertyType.tinyint)
  ))
