package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.given

private object CardPropertiesUUIDs:
  val uuid = "616c7919-9128-4095-94f0-a46248e071e8"
  val element = "da10c638-7e3c-4084-b745-10370fe02c1f"
  val types = "b942d73d-ff47-4a08-9315-53e6d88dbc0b"
  val classes = "6e27e02d-09a2-4db9-9b9e-13e349fa86d5"
  val subTypes = "4fa54ad8-c7c3-49ed-ae43-4c864585cce6"
  val effect = "cfb96e6f-31bd-42a2-8756-ab66ff8dbb37"
  val memoryCost = "932eec50-0e66-4198-a908-b0ae0e4049f5"
  val reserveCost = "78237d1d-8be0-4426-8111-d8ef4274f441"
  val level = "cc5cfe91-3914-4cd0-9f5b-57a11a3e5424"
  val speed = "9063cf0a-6cfc-4815-bb85-075f3c15431e"
  val legality = "91f54686-c621-49e8-b379-76b408d1fefa"
  val power = "73a43554-8614-4e1a-a485-7396ba564149"
  val life = "56c3410e-188c-4923-a0f1-64c4211a2919"
  val durability = "44a3605f-125a-44ce-961b-30b9c89b3976"
  val flavourText = "6edcb854-eab6-4f38-9544-a31ba6e5c448"

enum CardProperties(val property: Property):
  case uuid extends CardProperties(Property(
    pk = CardPropertiesUUIDs.uuid,
    propertyName = "UUID",
    propertyTypes = List(PropertyType.varchar),
    propertyValues = PropertyValues.singleValue
  ))
  case element extends CardProperties(Property(
    pk = CardPropertiesUUIDs.element,
    propertyName = "Element",
    propertyTypes = List(PropertyType.varchar),
    propertyValues = PropertyValues.singleValue
  ))
  case types extends CardProperties(Property(
    pk = CardPropertiesUUIDs.types,
    propertyName = "Card Typeline",
    propertyTypes = List(PropertyType.varchar),
  ))
  case classes extends CardProperties(Property(
    pk = CardPropertiesUUIDs.classes,
    propertyName = "Classes",
    propertyTypes = List(PropertyType.varchar)
  ))
  case subTypes extends CardProperties(Property(
    pk = CardPropertiesUUIDs.subTypes,
    propertyName = "Subtypes",
    propertyTypes = List(PropertyType.varchar)
  ))
  case effect extends CardProperties(Property(
    pk = CardPropertiesUUIDs.effect,
    propertyName = "Effect",
    propertyTypes = List(PropertyType.varchar)
  ))
  case memoryCost extends CardProperties(Property(
    pk = CardPropertiesUUIDs.memoryCost,
    propertyName = "Memory Cost",
    propertyTypes = List(PropertyType.tinyint),
    propertyValues = PropertyValues.singleValue
  ))
  case reserveCost extends CardProperties(Property(
    pk = CardPropertiesUUIDs.reserveCost,
    propertyName = "Reserve Cost",
    propertyTypes = List(PropertyType.tinyint),
    propertyValues = PropertyValues.singleValue
  ))
  case level extends CardProperties(Property(
    pk = CardPropertiesUUIDs.level,
    propertyName = "Level",
    propertyTypes = List(PropertyType.tinyint),
    propertyValues = PropertyValues.singleValue
  ))
  case speed extends CardProperties(Property(
    pk = CardPropertiesUUIDs.speed,
    propertyName = "Speed",
    propertyTypes = List(PropertyType.varchar),
  ))
  case legality extends CardProperties(Property(
    pk = CardPropertiesUUIDs.legality,
    propertyName = "Legality",
    propertyTypes = List(PropertyType.json),
  ))
  case power extends CardProperties(Property(
    pk = CardPropertiesUUIDs.power,
    propertyName = "Power",
    propertyTypes = List(PropertyType.tinyint),
    propertyValues = PropertyValues.singleValue
  ))
  case life extends CardProperties(Property(
    pk = CardPropertiesUUIDs.life,
    propertyName = "Life",
    propertyTypes = List(PropertyType.tinyint),
    propertyValues = PropertyValues.singleValue
  ))
  case durability extends CardProperties(Property(
    pk = CardPropertiesUUIDs.durability,
    propertyName = "Durability",
    propertyTypes = List(PropertyType.tinyint),
    propertyValues = PropertyValues.singleValue
  ))
