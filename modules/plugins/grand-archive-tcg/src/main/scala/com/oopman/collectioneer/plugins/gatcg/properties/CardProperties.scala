package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.HasProperty
import com.oopman.collectioneer.given

private object CardPropertiesUUIDs:
  val cardUID = "616c7919-9128-4095-94f0-a46248e071e8"
  val element = "da10c638-7e3c-4084-b745-10370fe02c1f"
  val types = "b942d73d-ff47-4a08-9315-53e6d88dbc0b"
  val classes = "6e27e02d-09a2-4db9-9b9e-13e349fa86d5"
  val subTypes = "4fa54ad8-c7c3-49ed-ae43-4c864585cce6"
  val effect = "cfb96e6f-31bd-42a2-8756-ab66ff8dbb37"
  val effectRaw = "a10e2c4f-e5c5-4dcc-8c7a-fa151cc35045"
  val memoryCost = "932eec50-0e66-4198-a908-b0ae0e4049f5"
  val reserveCost = "78237d1d-8be0-4426-8111-d8ef4274f441"
  val level = "cc5cfe91-3914-4cd0-9f5b-57a11a3e5424"
  val speed = "9063cf0a-6cfc-4815-bb85-075f3c15431e"
  val legality = "91f54686-c621-49e8-b379-76b408d1fefa"
  val power = "73a43554-8614-4e1a-a485-7396ba564149"
  val life = "56c3410e-188c-4923-a0f1-64c4211a2919"
  val durability = "44a3605f-125a-44ce-961b-30b9c89b3976"
  val flavourText = "6edcb854-eab6-4f38-9544-a31ba6e5c448"

object CardProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum CardProperties(val property: projected.Property):
  case cardUID extends CardProperties(Property(
    pk = CardPropertiesUUIDs.cardUID,
    propertyName = "Card UID",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case element extends CardProperties(Property(
    pk = CardPropertiesUUIDs.element,
    propertyName = "Element",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case types extends CardProperties(Property(
    pk = CardPropertiesUUIDs.types,
    propertyName = "Types",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case classes extends CardProperties(Property(
    pk = CardPropertiesUUIDs.classes,
    propertyName = "Classes",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case subTypes extends CardProperties(Property(
    pk = CardPropertiesUUIDs.subTypes,
    propertyName = "Subtypes",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case effect extends CardProperties(Property(
    pk = CardPropertiesUUIDs.effect,
    propertyName = "Effect",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case effectRaw extends CardProperties(Property(
    pk = CardPropertiesUUIDs.effectRaw,
    propertyName = "Raw Effect",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case flavourText extends CardProperties(Property(
    pk = CardPropertiesUUIDs.flavourText,
    propertyName = "Flavour text",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case memoryCost extends CardProperties(Property(
    pk = CardPropertiesUUIDs.memoryCost,
    propertyName = "Memory Cost",
    propertyTypes = PropertyType.smallint :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case reserveCost extends CardProperties(Property(
    pk = CardPropertiesUUIDs.reserveCost,
    propertyName = "Reserve Cost",
    propertyTypes = PropertyType.smallint :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case level extends CardProperties(Property(
    pk = CardPropertiesUUIDs.level,
    propertyName = "Level",
    propertyTypes = PropertyType.smallint :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case speed extends CardProperties(Property(
    pk = CardPropertiesUUIDs.speed,
    propertyName = "Speed",
    propertyTypes = PropertyType.boolean :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty,
  )) with HasProperty
  case legality extends CardProperties(Property(
    pk = CardPropertiesUUIDs.legality,
    propertyName = "Legality",
    propertyTypes = PropertyType.json :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty,
  )) with HasProperty
  case power extends CardProperties(Property(
    pk = CardPropertiesUUIDs.power,
    propertyName = "Power",
    propertyTypes = PropertyType.smallint :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case life extends CardProperties(Property(
    pk = CardPropertiesUUIDs.life,
    propertyName = "Life",
    propertyTypes = PropertyType.smallint :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case durability extends CardProperties(Property(
    pk = CardPropertiesUUIDs.durability,
    propertyName = "Durability",
    propertyTypes = PropertyType.smallint :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
