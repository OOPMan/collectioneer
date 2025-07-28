package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{EditionProperties, CommonProperties}

import java.util.UUID

object InnerEdition:
  extension (innerEdition: Models.InnerEdition)
    
    def asCollection(innerCardData: Collection): Collection =
      val name = innerCardData.propertyValues.get(CoreProperties.name).map(_.textValues).getOrElse(Nil)
      
      val editionName = s"(${innerEdition.set.prefix} - ${innerEdition.collector_number})" :: Nil
        Collection(
        pk = UUID.nameUUIDFromBytes(s"GATCG-inner-edition-${innerEdition.uuid}".getBytes),
        virtual = true,
        propertyValues = Map(
          CoreProperties.name -> PropertyValue(textValues = name ++ editionName),
          CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
          CommonProperties.isGATCGInnerEdition -> PropertyValue(booleanValues = true :: Nil),
          EditionProperties.editionUID -> PropertyValue(textValues = innerEdition.uuid :: Nil),
          EditionProperties.cardUID -> PropertyValue(textValues = innerEdition.card_id :: Nil),
          EditionProperties.collectorNumber -> PropertyValue(textValues = innerEdition.collector_number :: Nil),
          EditionProperties.illustrator -> PropertyValue(textValues = innerEdition.illustrator ++: Nil),
          EditionProperties.image -> PropertyValue(textValues = innerEdition.image :: Nil),
          EditionProperties.slug -> PropertyValue(textValues = innerEdition.slug :: Nil),
          EditionProperties.rarity -> PropertyValue(smallintValues = innerEdition.rarity.toShort :: Nil),
          EditionProperties.effect -> PropertyValue(textValues = innerEdition.effect ++: Nil),
          EditionProperties.effectRaw -> PropertyValue(textValues = innerEdition.effect_raw ++: Nil),
          EditionProperties.flavourText -> PropertyValue(textValues = innerEdition.flavor ++: Nil),
          EditionProperties.configuration -> PropertyValue(textValues = innerEdition.configuration ++: Nil),
          EditionProperties.orientation -> PropertyValue(textValues = innerEdition.orientation ++: Nil),
        )
      )


