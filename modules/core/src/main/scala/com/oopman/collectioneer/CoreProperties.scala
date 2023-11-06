package com.oopman.collectioneer

import com.oopman.collectioneer.given
import com.oopman.collectioneer.db.entity.PropertyType
import com.oopman.collectioneer.db.entity.raw.Property

import java.util.UUID

/**
 * This enumeration contains the UUIDs and PropertyTypes associated with the core properties in the application
 */
enum CoreProperties(val property: Property):
  case name extends CoreProperties(Property(
    "f65d1d21-3542-48f9-a9d5-f96921e4ba15",
    "Name",
    List(PropertyType.varchar)
  ))
  case description extends CoreProperties(Property(
    "ea3c3226-2a04-45f0-a50e-1db9c4aa0072",
    "Description",
    List(PropertyType.varchar, PropertyType.clob)
  ))
  case default_value extends CoreProperties(Property(
    "7b7b9577-da92-41d2-9ff8-a174e191b030",
    "Default Value",
    PropertyType.values.toList
  ))
  case min_value extends CoreProperties(Property(
    "4d3460bd-2cbf-45f9-9059-59edea9c1a17",
    "Minimum Value",
    PropertyType.values.toList
  ))
  case max_value extends CoreProperties(Property(
    "9eeca250-53c8-4a35-a636-6954d0718b93",
    "Maximum Value",
    PropertyType.values.toList
  ))
  case min_values extends CoreProperties(Property(
    "df39b389-5055-46f7-82f3-53c736fb6950",
    "Minimum Number of Values",
    List(PropertyType.int)
  ))
  case max_values extends CoreProperties(Property(
    "158a2358-b5d1-4e73-a6bf-c93d0587590d",
    "Maximum Number of Values",
    List(PropertyType.int)
  ))

