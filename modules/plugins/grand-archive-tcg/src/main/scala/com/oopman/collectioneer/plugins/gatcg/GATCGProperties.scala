package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.given
import com.oopman.collectioneer.db.entity.{Property, PropertyType}


enum GATCGProperties(property: Property) {
  case reserveCost extends GATCGProperties(Property(
    pk="78237d1d-8be0-4426-8111-d8ef4274f441",
    propertyName="Reserve Cost",
    propertyTypes=List(PropertyType.tinyint)
  ))
}