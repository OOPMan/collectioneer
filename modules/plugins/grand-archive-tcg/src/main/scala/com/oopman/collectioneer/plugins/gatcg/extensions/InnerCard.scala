package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CardProperties, CommonProperties}

import java.util.UUID

object InnerCard:
  extension (innerCard: Models.InnerCard)
    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes (s"GATCG-inner-card-${innerCard.uuid}-${innerCard.edition_id}".getBytes),
      virtual = true,
      propertyValues = Map(
        CoreProperties.name -> PropertyValue(stringValues = innerCard.name :: Nil),
        CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
        CommonProperties.isGATCGInnerCard -> PropertyValue(booleanValues = true :: Nil),
        CardProperties.cardUID -> PropertyValue (stringValues = innerCard.uuid :: Nil),
        CardProperties.element -> PropertyValue (stringValues = innerCard.element :: Nil),
        CardProperties.types -> PropertyValue (stringValues = innerCard.types),
        CardProperties.classes -> PropertyValue (stringValues = innerCard.classes),
        CardProperties.subTypes -> PropertyValue (stringValues = innerCard.subtypes),
        CardProperties.effect -> PropertyValue (stringValues = innerCard.effect ++: Nil),
        CardProperties.effectRaw -> PropertyValue (stringValues = innerCard.effect_raw ++: Nil),
        CardProperties.flavourText -> PropertyValue (stringValues = innerCard.flavor ++: Nil),
        CardProperties.memoryCost -> PropertyValue (shortValues = innerCard.cost_memory.map(_.toShort) ++: Nil),
        CardProperties.reserveCost -> PropertyValue (shortValues = innerCard.cost_reserve.map(_.toShort) ++: Nil),
        CardProperties.level -> PropertyValue (shortValues = innerCard.level.map (_.toShort) ++: Nil),
        CardProperties.speed -> PropertyValue (booleanValues = innerCard.speed ++: Nil),
        CardProperties.legality -> PropertyValue (jsonValues = innerCard.legality ++: Nil),
        CardProperties.power -> PropertyValue (shortValues = innerCard.power.map (_.toShort) ++: Nil),
        CardProperties.life -> PropertyValue (shortValues = innerCard.life.map (_.toShort) ++: Nil),
        CardProperties.durability -> PropertyValue (shortValues = innerCard.durability.map (_.toShort) ++: Nil),
      )
    )