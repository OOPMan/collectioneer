package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.given

/**
 * EditionProperties are properties of the relationship between a Card and a Set
 */

private object EditionPropertiesUUIDs:
  val collectorNumber = "6628f2a8-0f5c-4177-8db4-70de9a095ecc"
  val illustrator = "7772da6d-c693-4f1f-a074-6c161f5fabb0"
  val indexUrl = "71f4b797-7e90-4ab3-a5e1-2330d2d7b23e"
  val imageUrl = "94d5972e-0c5b-43e3-8b39-33122249b52d"
  val rarity = "2a9e5492-f032-4a4b-b01b-379ff7197f8a"
  val foilPopulation = "ca0ce592-2538-465b-8a04-bc6d80ff2a6d"
  val normalPopulation = "82093b42-66ed-4a90-801f-0022b4389009"

enum EditionProperties(val property: Property):
  case collectorNumber extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.collectorNumber,
    propertyName = "Collector Number",
    propertyTypes = List(PropertyType.varchar)
  ))
  case illustrator extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.illustrator,
    propertyName = "Artist",
    propertyTypes = List(PropertyType.varchar)
  ))