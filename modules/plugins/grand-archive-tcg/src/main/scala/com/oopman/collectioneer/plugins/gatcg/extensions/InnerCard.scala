package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CardProperties, CommonProperties}

import java.util.UUID

object InnerCard:
  extension (innerCard: Models.InnerCard)
    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes (s"GATCG-inner-card-${innerCard.uuid}-${innerCard.edition_id}".getBytes),
      virtual = true,
      propertyValues = Map(
        CoreProperties.name -> PropertyValue(textValues = innerCard.name :: Nil),
        CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
        CommonProperties.isGATCGInnerCard -> PropertyValue(booleanValues = true :: Nil),
        CardProperties.cardUID -> PropertyValue (textValues = innerCard.uuid :: Nil),
        CardProperties.element -> PropertyValue (textValues = innerCard.element :: Nil),
        CardProperties.types -> PropertyValue (textValues = innerCard.types),
        CardProperties.classes -> PropertyValue (textValues = innerCard.classes),
        CardProperties.subTypes -> PropertyValue (textValues = innerCard.subtypes),
        CardProperties.effect -> PropertyValue (textValues = innerCard.effect ++: Nil),
        CardProperties.effectRaw -> PropertyValue (textValues = innerCard.effect_raw ++: Nil),
        CardProperties.flavourText -> PropertyValue (textValues = innerCard.flavor ++: Nil),
        CardProperties.memoryCost -> PropertyValue (smallintValues = innerCard.cost_memory.map(_.toShort) ++: Nil),
        CardProperties.reserveCost -> PropertyValue (smallintValues = innerCard.cost_reserve.map(_.toShort) ++: Nil),
        CardProperties.level -> PropertyValue (smallintValues = innerCard.level.map (_.toShort) ++: Nil),
        CardProperties.speed -> PropertyValue (booleanValues = innerCard.speed ++: Nil),
        CardProperties.legality -> PropertyValue (jsonValues = innerCard.legality ++: Nil),
        CardProperties.power -> PropertyValue (smallintValues = innerCard.power.map (_.toShort) ++: Nil),
        CardProperties.life -> PropertyValue (smallintValues = innerCard.life.map (_.toShort) ++: Nil),
        CardProperties.durability -> PropertyValue (smallintValues = innerCard.durability.map (_.toShort) ++: Nil),
      )
    )