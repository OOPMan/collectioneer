package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.given

/**
 * EditionProperties are properties of the relationship between a Card and a Set
 */

private object EditionPropertiesUUIDs:
  val uuid = "464603e5-e9cd-4508-97cc-cbf36426d366"
  val cardId = "1012fb83-0920-4e42-9020-bd6c754e6e49"
  val collectorNumber = "6628f2a8-0f5c-4177-8db4-70de9a095ecc"
  val illustrator = "7772da6d-c693-4f1f-a074-6c161f5fabb0"
  val slug = "ff777245-92fd-46b0-908f-fe5152dae509"
  val rarity = "2a9e5492-f032-4a4b-b01b-379ff7197f8a"
  val effect = "40513d4c-2fae-4d23-9c85-cb0df15dbb4c"
  val flavourText = "49aa3c45-2a44-4e52-9e9b-b4ef7513498f"

enum EditionProperties(val property: Property):
  case uuid extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.uuid,
    propertyName = "UUID",
    propertyTypes = List(PropertyType.varchar),
    propertyValues = PropertyValues.singleValue
  ))
  case cardId extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.cardId,
    propertyName = "Card UUID",
    propertyTypes = List(PropertyType.varchar),
    propertyValues = PropertyValues.singleValue
  ))
  case collectorNumber extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.collectorNumber,
    propertyName = "Collector Number",
    propertyTypes = List(PropertyType.varchar),
    propertyValues = PropertyValues.singleValue
  ))
  case illustrator extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.illustrator,
    propertyName = "Artist",
    propertyTypes = List(PropertyType.varchar),
    propertyValues = PropertyValues.singleValue
  ))
  case slug extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.slug,
    propertyName = "Slug",
    propertyTypes = List(PropertyType.varchar),
    propertyValues = PropertyValues.singleValue
  ))
  case rarity extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.rarity,
    propertyName = "Rarity",
    propertyTypes = List(PropertyType.tinyint),
    propertyValues = PropertyValues.singleValue
  ))
  case effect extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.effect,
    propertyName = "Effect",
    propertyTypes = List(PropertyType.varchar),
  ))
  case flavourText extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.flavourText,
    propertyName = "Flavour text",
    propertyTypes = List(PropertyType.varchar)
  ))
