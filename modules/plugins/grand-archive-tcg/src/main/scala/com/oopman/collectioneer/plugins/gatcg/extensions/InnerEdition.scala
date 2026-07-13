package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, EditionProperties}

import java.util.UUID

object InnerEdition:
  extension (innerEdition: Models.InnerEdition)
    
    def asCollection(innerCardData: Collection): Collection =
      val name = innerCardData.propertyValues.get(CoreProperties.name).map(_.stringValues).getOrElse(Nil)
      
      val editionName = s"(${innerEdition.set.prefix} - ${innerEdition.collector_number})" :: Nil
        Collection(
        pk = UUID.nameUUIDFromBytes(s"GATCG-inner-edition-${innerEdition.uuid}".getBytes),
        virtual = true,
        propertyValues = Map(
          CoreProperties.name -> PropertyValue(stringValues = name ++ editionName),
          CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
          CommonProperties.isGATCGInnerEdition -> PropertyValue(booleanValues = true :: Nil),
          EditionProperties.editionUID -> PropertyValue(stringValues = innerEdition.uuid :: Nil),
          EditionProperties.cardUID -> PropertyValue(stringValues = innerEdition.card_id :: Nil),
          EditionProperties.collectorNumber -> PropertyValue(stringValues = innerEdition.collector_number :: Nil),
          EditionProperties.illustrator -> PropertyValue(stringValues = innerEdition.illustrator ++: Nil),
          EditionProperties.image -> PropertyValue(stringValues = innerEdition.image :: Nil),
          EditionProperties.slug -> PropertyValue(stringValues = innerEdition.slug :: Nil),
          EditionProperties.rarity -> PropertyValue(shortValues = innerEdition.rarity.toShort :: Nil),
          EditionProperties.effect -> PropertyValue(stringValues = innerEdition.effect ++: Nil),
          EditionProperties.effectRaw -> PropertyValue(stringValues = innerEdition.effect_raw ++: Nil),
          EditionProperties.flavourText -> PropertyValue(stringValues = innerEdition.flavor ++: Nil),
          EditionProperties.configuration -> PropertyValue(stringValues = innerEdition.configuration ++: Nil),
          EditionProperties.orientation -> PropertyValue(stringValues = innerEdition.orientation ++: Nil),
        )
      )


