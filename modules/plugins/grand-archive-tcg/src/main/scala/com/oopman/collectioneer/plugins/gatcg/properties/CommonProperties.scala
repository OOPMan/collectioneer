package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.HasProperty
import com.oopman.collectioneer.{CoreProperties, given}
import com.oopman.collectioneer.db.traits.entity.raw.given

private object CommonPropertiesUUIDs:
  val isGATCGProperty = "91fb16e6-68f8-444a-acde-f1daceb768cc"
  val isGATCGCollection = "d6259294-bb14-48ab-822d-698457510e4c"
  val isGATCGSet = "84ab0f47-a357-4376-a3a9-e38845f2f87c"
  val isGATCGSetCollection = "c8d11a78-4ea0-435b-b9ea-30590e08c5a5"
  val isGATCGCardCollection = "90f467f1-12ea-4c12-b37d-7ed5573790e3"
  val isGATCGCard = "015d940f-05d6-4f23-9a2b-fe5b8c1fe654"
  val isGATCGEdition = "96a544e7-0580-436c-8429-ccea416a488c"
  val isGATCGCirculation = "e62a5a99-d946-476d-8b37-1374238e0132"
  val isGATCGRule = "e246bb8f-9fb3-498b-ba3f-d4f109d507ba"
  val isGATCGReference = "73bb716a-a9bc-47ae-b418-4f8732c5f5e9"

private val isGATCGProperty = Property(
  pk = CommonPropertiesUUIDs.isGATCGProperty,
  propertyName = "Is GATCG Property",
  propertyTypes = List(PropertyType.boolean),
  propertyValues = Map(
    CoreProperties.minValues -> PropertyValue(smallintValues = List(1)),
    CoreProperties.maxValues -> PropertyValue(smallintValues = List(1))
  )
)

object CommonProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum CommonProperties(val property: projected.Property) :
  case isGATCGProperty extends CommonProperties(com.oopman.collectioneer.plugins.gatcg.properties.isGATCGProperty) with HasProperty
  case isGATCGCollection extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGCollection,
    propertyName = "Is GATCG Collection",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGSet extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGSet,
    propertyName = "Is GATCG Set",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGSetCollection extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGSetCollection,
    propertyName = "Is GATCG Set Collection",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGCardCollection extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGCardCollection,
    propertyName = "Is GATCG Card Collection",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGCard extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGCard,
    propertyName = "Is GATCG Card",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGEdition extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGEdition,
    propertyName = "Is GATCG Edition",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGCirculation extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGCirculation,
    propertyName = "Is GATCG Circulation",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGRule extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGRule,
    propertyName = "Is GATCG Rule",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case isGATCGReference extends CommonProperties(Property(
    pk = CommonPropertiesUUIDs.isGATCGReference,
    propertyName = "Is GATCG Reference",
    propertyTypes = List(PropertyType.boolean),
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty