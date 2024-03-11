package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.{CoreProperties, given}

private object CardPropertiesUUIDs:
  val memoryCost = "932eec50-0e66-4198-a908-b0ae0e4049f5"
  val reserveCost = "78237d1d-8be0-4426-8111-d8ef4274f441"
  val element = "da10c638-7e3c-4084-b745-10370fe02c1f"
  val types = "b942d73d-ff47-4a08-9315-53e6d88dbc0b"
  val classes = "6e27e02d-09a2-4db9-9b9e-13e349fa86d5"
  val subTypes = "4fa54ad8-c7c3-49ed-ae43-4c864585cce6"
  val speed = "9063cf0a-6cfc-4815-bb85-075f3c15431e"
  val power = "73a43554-8614-4e1a-a485-7396ba564149"
  val life = "56c3410e-188c-4923-a0f1-64c4211a2919"
  val durability = "44a3605f-125a-44ce-961b-30b9c89b3976"
  val flavourText = "6edcb854-eab6-4f38-9544-a31ba6e5c448"
  // TODO: Add legality?

enum CardProperties(val property: Property):
  case cost extends CardProperties(Property(
    pk = CardPropertiesUUIDs.reserveCost,
    propertyName = "Cost",
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
  case element extends CardProperties(Property(
    pk = CardPropertiesUUIDs.element,
    propertyName = "Element",
    propertyTypes = List(PropertyType.varchar),
  ))
  case types extends CardProperties(Property(
    pk = CardPropertiesUUIDs.types,
    propertyName = "Card Typeline",
    propertyTypes = List(PropertyType.varchar),
  ))
  case speed extends CardProperties(Property(
    pk = CardPropertiesUUIDs.speed,
    propertyName = "Speed",
    propertyTypes = List(PropertyType.varchar),
  ))
  case power extends CardProperties(Property(
    pk = CardPropertiesUUIDs.power,
    propertyName = "Power",
    propertyTypes = List(PropertyType.tinyint),
  ))
  case life extends CardProperties(Property(
    pk = CardPropertiesUUIDs.life,
    propertyName = "Life",
    propertyTypes = List(PropertyType.tinyint),
  ))
  case durability extends CardProperties(Property(
    pk = CardPropertiesUUIDs.durability,
    propertyName = "Durability",
    propertyTypes = List(PropertyType.tinyint),
  ))
