package com.oopman.collectioneer

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.given

object CorePropertyUUIDs:
  val name = "f65d1d21-3542-48f9-a9d5-f96921e4ba15"
  val description = "ea3c3226-2a04-45f0-a50e-1db9c4aa0072"
  val defaultValue = "7b7b9577-da92-41d2-9ff8-a174e191b030"
  val minValue = "4d3460bd-2cbf-45f9-9059-59edea9c1a17"
  val maxValue = "9eeca250-53c8-4a35-a636-6954d0718b93"
  val minValues = "df39b389-5055-46f7-82f3-53c736fb6950"
  val maxValues = "158a2358-b5d1-4e73-a6bf-c93d0587590d"
  val visible = "baa75583-d622-47ab-962e-444693bc4af5"

/**
 * This enumeration contains the UUIDs and PropertyTypes associated with the core properties in the application
 */
enum CoreProperties(val property: Property):
  case name extends CoreProperties(Property(
    pk = CorePropertyUUIDs.name,
    propertyName = "Name",
    propertyTypes = List(PropertyType.text),
  ))
  case description extends CoreProperties(Property(
    pk = CorePropertyUUIDs.description,
    propertyName = "Description",
    propertyTypes = List(PropertyType.text),
  ))
  case defaultValue extends CoreProperties(Property(
    pk = CorePropertyUUIDs.defaultValue,
    propertyName = "Default Value",
    propertyTypes = PropertyType.values.toList,
  ))
  case minValue extends CoreProperties(Property(
    pk = CorePropertyUUIDs.minValue,
    propertyName = "Minimum Value",
    propertyTypes = PropertyType.values.toList,
  ))
  case maxValue extends CoreProperties(Property(
    pk = CorePropertyUUIDs.maxValue,
    propertyName = "Maximum Value",
    propertyTypes = PropertyType.values.toList,
  ))
  case minValues extends CoreProperties(Property(
    pk = CorePropertyUUIDs.minValues,
    propertyName = "Minimum Number of Values",
    propertyTypes = List(PropertyType.int),
  ))
  case maxValues extends CoreProperties(Property(
    pk = CorePropertyUUIDs.maxValues,
    propertyName = "Maximum Number of Values",
    propertyTypes = List(PropertyType.int),
  ))
  case visible extends CoreProperties(Property(
    pk = CorePropertyUUIDs.visible,
    propertyName = "Visible",
    propertyTypes = List(PropertyType.boolean)
  ))
