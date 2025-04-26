package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{EditionProperties, CommonProperties}

import java.util.UUID

object InnerEdition:
  extension (innerEdition: Models.InnerEdition)
    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes(s"GATCG-inner-edition-${innerEdition.uuid}".getBytes),
      virtual = true,
      propertyValues = Map(
        CommonProperties.isGATCGEdition -> PropertyValue(booleanValues = true :: Nil),
        EditionProperties.editionUID -> PropertyValue(textValues = innerEdition.uuid :: Nil),
        EditionProperties.cardUID -> PropertyValue(textValues = innerEdition.card_id :: Nil),
        EditionProperties.collectorNumber -> PropertyValue(textValues = innerEdition.collector_number :: Nil),
        EditionProperties.illustrator -> PropertyValue(textValues = innerEdition.illustrator.map(_ :: Nil).getOrElse(Nil)),
        EditionProperties.image -> PropertyValue(textValues = innerEdition.image :: Nil),
        EditionProperties.slug -> PropertyValue(textValues = innerEdition.slug :: Nil),
        EditionProperties.rarity -> PropertyValue(smallintValues = innerEdition.rarity.toShort :: Nil), // TODO: Covert to String
        EditionProperties.effect -> PropertyValue(textValues = innerEdition.effect.map(_ :: Nil).getOrElse(Nil)),
        EditionProperties.flavourText -> PropertyValue(textValues = innerEdition.flavor.map(_ :: Nil).getOrElse(Nil)),
        EditionProperties.configuration -> PropertyValue(textValues = innerEdition.configuration.map(_ :: Nil).getOrElse(Nil)),
        EditionProperties.orientation -> PropertyValue(textValues = innerEdition.orientation.map(_ :: Nil).getOrElse(Nil)),
      )
    )


