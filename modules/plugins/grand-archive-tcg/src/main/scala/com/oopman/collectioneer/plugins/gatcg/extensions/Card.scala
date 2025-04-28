package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CardProperties, CommonProperties}

import java.util.UUID

object Card:
  extension (card: Models.Card)
    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes (s"GATCG-card-${card.uuid}".getBytes),
      virtual = true,
      propertyValues = Map (
        CoreProperties.name -> PropertyValue (textValues = card.name :: Nil),
        CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
        CommonProperties.isGATCGCard -> PropertyValue (booleanValues = true :: Nil),
        CardProperties.cardUID -> PropertyValue (textValues = card.uuid :: Nil),
        CardProperties.element -> PropertyValue (textValues = card.element :: Nil),
        CardProperties.types -> PropertyValue (textValues = card.types),
        CardProperties.classes -> PropertyValue (textValues = card.classes),
        CardProperties.subTypes -> PropertyValue (textValues = card.subtypes),
        CardProperties.effect -> PropertyValue (textValues = card.effect_raw.map (_:: Nil).getOrElse (Nil)),
        CardProperties.memoryCost -> PropertyValue (smallintValues = card.cost_memory.map (_.toShort :: Nil).getOrElse (Nil)),
        CardProperties.reserveCost -> PropertyValue (smallintValues = card.cost_reserve.map (_.toShort :: Nil).getOrElse (Nil)),
        CardProperties.level -> PropertyValue (smallintValues = card.level.map (_.toShort :: Nil).getOrElse (Nil)),
        CardProperties.speed -> PropertyValue (booleanValues = card.speed.getOrElse(false) :: Nil),
        CardProperties.legality -> PropertyValue (jsonValues = card.legality.map (_:: Nil).getOrElse (Nil)),
        CardProperties.power -> PropertyValue (smallintValues = card.power.map (_.toShort :: Nil).getOrElse (Nil)),
        CardProperties.life -> PropertyValue (smallintValues = card.life.map (_.toShort :: Nil).getOrElse (Nil)),
        CardProperties.durability -> PropertyValue (smallintValues = card.durability.map (_.toShort :: Nil).getOrElse (Nil)),
      )
    )