package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{EditionProperties, CommonProperties}

import java.util.UUID

object Edition:
  extension (edition: Models.Edition)

    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes(s"GATCG-edition-${edition.uuid}".getBytes),
      virtual = true,
      propertyValues = Map(
        CommonProperties.isGATCGEdition -> PropertyValue(booleanValues = true :: Nil),
        EditionProperties.editionUID -> PropertyValue(textValues = edition.uuid :: Nil),
        EditionProperties.cardUID -> PropertyValue(textValues = edition.card_id :: Nil),
        EditionProperties.collectorNumber -> PropertyValue(textValues = edition.collector_number :: Nil),
        EditionProperties.illustrator -> PropertyValue(textValues = edition.illustrator.map(_ :: Nil).getOrElse(Nil)),
        EditionProperties.image -> PropertyValue(textValues = edition.image :: Nil),
        EditionProperties.slug -> PropertyValue(textValues = edition.slug :: Nil),
        EditionProperties.rarity -> PropertyValue(smallintValues = edition.rarity.toShort :: Nil),
        EditionProperties.effect -> PropertyValue(textValues = edition.effect.map(_ :: Nil).getOrElse(Nil)),
        EditionProperties.flavourText -> PropertyValue(textValues = edition.flavor.map(_ :: Nil).getOrElse(Nil)),
        EditionProperties.configuration -> PropertyValue(textValues = edition.configuration.map(_ :: Nil).getOrElse(Nil)),
        EditionProperties.orientation -> PropertyValue(textValues = edition.orientation.map(_ :: Nil).getOrElse(Nil)),
      )
    )
