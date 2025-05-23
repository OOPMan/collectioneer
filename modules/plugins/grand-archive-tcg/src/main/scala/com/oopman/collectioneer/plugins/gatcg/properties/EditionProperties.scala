package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.HasProperty
import com.oopman.collectioneer.given

/**
 * EditionProperties are properties of the relationship between a Card and a Set
 */

private object EditionPropertiesUUIDs:
  val editionUID = "464603e5-e9cd-4508-97cc-cbf36426d366"
  val cardUID = "1012fb83-0920-4e42-9020-bd6c754e6e49"
  val collectorNumber = "6628f2a8-0f5c-4177-8db4-70de9a095ecc"
  val illustrator = "7772da6d-c693-4f1f-a074-6c161f5fabb0"
  val image = "27b7ea66-0710-4d3c-a488-8cc972ded3bf"
  val slug = "ff777245-92fd-46b0-908f-fe5152dae509"
  val rarity = "2a9e5492-f032-4a4b-b01b-379ff7197f8a"
  val effect = "40513d4c-2fae-4d23-9c85-cb0df15dbb4c"
  val flavourText = "49aa3c45-2a44-4e52-9e9b-b4ef7513498f"
  val configuration = "ad41846c-0372-4e93-a425-878d433bd70c"
  val orientation = "f921e51b-f273-4066-927f-ac0ecf6e1cee"

object EditionProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum EditionProperties(val property: projected.Property):
  case editionUID extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.editionUID,
    propertyName = "Edition UID",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  // TODO: We can probably remove this or mark it has hidden
  case cardUID extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.cardUID,
    propertyName = "Card UID",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case collectorNumber extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.collectorNumber,
    propertyName = "Collector Number",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case illustrator extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.illustrator,
    propertyName = "Artist",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case image extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.image,
    propertyName = "Image",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case slug extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.slug,
    propertyName = "Slug",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  // TODO: Add a property for storing downloaded image data?
  case rarity extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.rarity,
    propertyName = "Rarity",
    propertyTypes = PropertyType.smallint :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case effect extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.effect,
    propertyName = "Effect",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty,
  )) with HasProperty
  case flavourText extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.flavourText,
    propertyName = "Flavour text",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.visibleGATCGProperty
  )) with HasProperty
  case configuration extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.configuration,
    propertyName = "Configuration",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
  case orientation extends EditionProperties(Property(
    pk = EditionPropertiesUUIDs.orientation,
    propertyName = "Orientation",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
