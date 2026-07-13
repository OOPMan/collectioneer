package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CardProperties, CommonProperties}

import java.util.UUID

object Card:
  extension (card: Models.Card)
    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes (s"GATCG-card-${card.uuid}".getBytes),
      virtual = true,
      propertyValues = Map (
        CoreProperties.name -> PropertyValue (stringValues = card.name :: Nil),
        CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
        CommonProperties.isGATCGCard -> PropertyValue (booleanValues = true :: Nil),
        CardProperties.cardUID -> PropertyValue (stringValues = card.uuid :: Nil),
        CardProperties.element -> PropertyValue (stringValues = card.element :: Nil),
        CardProperties.types -> PropertyValue (stringValues = card.types),
        CardProperties.classes -> PropertyValue (stringValues = card.classes),
        CardProperties.subTypes -> PropertyValue (stringValues = card.subtypes),
        CardProperties.effect -> PropertyValue (stringValues = card.effect ++: Nil),
        CardProperties.effectRaw -> PropertyValue (stringValues = card.effect_raw ++: Nil),
        CardProperties.flavourText -> PropertyValue (stringValues = card.flavor ++: Nil),
        CardProperties.memoryCost -> PropertyValue (shortValues = card.cost_memory.map(_.toShort) ++: Nil),
        CardProperties.reserveCost -> PropertyValue (shortValues = card.cost_reserve.map(_.toShort) ++: Nil),
        CardProperties.level -> PropertyValue (shortValues = card.level.map (_.toShort) ++: Nil),
        CardProperties.speed -> PropertyValue (booleanValues = card.speed ++: Nil),
        CardProperties.legality -> PropertyValue (jsonValues = card.legality ++: Nil),
        CardProperties.power -> PropertyValue (shortValues = card.power.map (_.toShort) ++: Nil),
        CardProperties.life -> PropertyValue (shortValues = card.life.map (_.toShort) ++: Nil),
        CardProperties.durability -> PropertyValue (shortValues = card.durability.map (_.toShort) ++: Nil),
      )
    )