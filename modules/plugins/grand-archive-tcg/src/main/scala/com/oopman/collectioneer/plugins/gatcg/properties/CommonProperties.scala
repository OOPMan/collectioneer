package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.given

private object CommonPropertiesUUIDs:
  val isGATCGSet = "c8d11a78-4ea0-435b-b9ea-30590e08c5a5"
  val isGATCGCard = "015d940f-05d6-4f23-9a2b-fe5b8c1fe654"
  val isGATCGEdition = "96a544e7-0580-436c-8429-ccea416a488c"

enum CommonProperties(val property: Property) :
  case isGATCGSet extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGSet,
    propertyName = "Is GATCG Set",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue
  )) with HasProperty
  case isGATCGCard extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGCard,
    propertyName = "Is GATCG Card",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue
  )) with HasProperty
  case isGATCGEdition extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGEdition,
    propertyName = "Is GATCG Edition",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue
  )) with HasProperty