package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.{CoreProperties, given}

private object CommonPropertiesUUIDs:
  val isGATCGSet = "84ab0f47-a357-4376-a3a9-e38845f2f87c"
  val isGATCGSetData = "c8d11a78-4ea0-435b-b9ea-30590e08c5a5"
  val isGATCGCard = "90f467f1-12ea-4c12-b37d-7ed5573790e3"
  val isGATCGCardData = "015d940f-05d6-4f23-9a2b-fe5b8c1fe654"
  val isGATCGEditionData = "96a544e7-0580-436c-8429-ccea416a488c"
  val isGATCGProperty = "91fb16e6-68f8-444a-acde-f1daceb768cc"

private val isGATCGProperty = Property(
  pk = CommonPropertiesUUIDs.isGATCGProperty,
  propertyName = "Is GATCG Property",
  propertyTypes = List(PropertyType.boolean),
  propertyValues = List(
    PropertyValue(
      property = CoreProperties.minValues,
      smallintValues = List(1)
    ),
    PropertyValue(
      property = CoreProperties.maxValues,
      smallintValues = List(1)
    ))
)

enum CommonProperties(val property: Property) :
  case isGATCGProperty extends CommonProperties(com.oopman.collectioneer.plugins.gatcg.properties.isGATCGProperty) with HasProperty
  case isGATCGSet extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGSet,
    propertyName = "Is GATCG Set Collection",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGSetData extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGSetData,
    propertyName = "Is GATCG Set Data",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGCard extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGCard,
    propertyName = "Is GATCG Card Collection",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGCardData extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGCardData,
    propertyName = "Is GATCG Card Data",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGEditionData extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGEditionData,
    propertyName = "Is GATCG Edition Data",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty