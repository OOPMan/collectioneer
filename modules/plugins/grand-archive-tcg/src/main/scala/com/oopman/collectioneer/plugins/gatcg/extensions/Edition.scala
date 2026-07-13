package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, EditionProperties}

import java.util.UUID

object Edition:
  extension (edition: Models.Edition)

    def asCollection(cardData: Collection): Collection = {
      val name = cardData.propertyValues.get(CoreProperties.name).map(_.stringValues).getOrElse(Nil)
      val circulationNames = edition.circulationTemplates.map(_.name).mkString(", ")
      val editionName = s"(${edition.set.prefix} - ${edition.collector_number} - $circulationNames)" :: Nil
      Collection(
        pk = UUID.nameUUIDFromBytes(s"GATCG-edition-${edition.uuid}".getBytes),
        virtual = true,
        propertyValues = Map(
          CoreProperties.name -> PropertyValue(stringValues = name ++ editionName),
          CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
          CommonProperties.isGATCGEdition -> PropertyValue(booleanValues = true :: Nil),
          EditionProperties.editionUID -> PropertyValue(stringValues = edition.uuid :: Nil),
          EditionProperties.cardUID -> PropertyValue(stringValues = edition.card_id :: Nil),
          EditionProperties.collectorNumber -> PropertyValue(stringValues = edition.collector_number :: Nil),
          EditionProperties.illustrator -> PropertyValue(stringValues = edition.illustrator ++: Nil),
          EditionProperties.image -> PropertyValue(stringValues = edition.image :: Nil),
          EditionProperties.slug -> PropertyValue(stringValues = edition.slug :: Nil),
          EditionProperties.rarity -> PropertyValue(shortValues = edition.rarity.toShort :: Nil),
          EditionProperties.effect -> PropertyValue(stringValues = edition.effect ++: Nil),
          EditionProperties.effectRaw -> PropertyValue(stringValues = edition.effect_raw ++: Nil),
          EditionProperties.flavourText -> PropertyValue(stringValues = edition.flavor ++: Nil),
          EditionProperties.configuration -> PropertyValue(stringValues = edition.configuration ++: Nil),
          EditionProperties.orientation -> PropertyValue(stringValues = edition.orientation ++: Nil),
        )
      )
    }
