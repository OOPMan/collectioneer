package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.properties.{CardProperties, CirculationProperties, EditionProperties, ReferenceProperties, RuleProperties, SetProperties}

case class Reference
(collection: projected.Collection) extends projected.HasCollection:
  val kind: String = collection.propertyValues(ReferenceProperties.kind).textValues.head
  val name: String = collection.propertyValues(CoreProperties.name).textValues.head
  val slug: String = collection.propertyValues(ReferenceProperties.slug).textValues.head
  val direction: String = collection.propertyValues(ReferenceProperties.direction).textValues.head

case class Rule
(collection: projected.Collection) extends projected.HasCollection:
  val title: String = collection.propertyValues(CoreProperties.name).textValues.head
  val description: String = collection.propertyValues(CoreProperties.description).textValues.head
  val dateAdded: String = collection.propertyValues(RuleProperties.dateAdded).textValues.head

case class Circulation
(collection: projected.Collection) extends projected.HasCollection:
  val name: Seq[String] = collection.propertyValues(CoreProperties.name).textValues
  val foil: Option[Boolean] = collection.propertyValues.get(CirculationProperties.foil).flatMap(_.booleanValues.headOption)
  val population: Int = collection.propertyValues(CirculationProperties.population).intValues.head
  
trait EditionCommon:
  val editionUID: String
  val cardUID: String
  val name: Seq[String]
  val collectorNumber: String
  val illustrator: Option[String]
  val image: String
  val slug: String
  val rarity: Short
  val effect: Option[String]
  val effectRaw: Option[String]
  val flavourText: Option[String]
  val configuration: Option[String]
  val orientation: Option[String]
  
trait CardCommon:
  val name: String
  val cardUID: String
  val element: String
  val types: Seq[String]
  val classes: Seq[String]
  val subTypes: Seq[String]
  val effect: Option[String]
  val effectRaw: Option[String]
  val memoryCost: Option[Short]
  val reserveCost: Option[Short]
  val level: Option[Short]
  val speed: Option[String]
  val legality: Option[io.circe.Json]
  val power: Option[Short]
  val life: Option[Short]
  val durability: Option[Short]
  

case class InnerEdition
(
  collection: projected.Collection,
  set: SetData
) extends projected.HasCollection, EditionCommon:
  val editionUID: String = collection.propertyValues(EditionProperties.editionUID).textValues.head
  val cardUID: String = collection.propertyValues(EditionProperties.cardUID).textValues.head
  val name: Seq[String] = collection.propertyValues(CoreProperties.name).textValues
  val collectorNumber: String = collection.propertyValues(EditionProperties.collectorNumber).textValues.head
  val illustrator: Option[String] = collection.propertyValues.get(EditionProperties.illustrator).flatMap(_.textValues.headOption)
  val image: String = collection.propertyValues(EditionProperties.image).textValues.head
  val slug: String = collection.propertyValues(EditionProperties.slug).textValues.head
  val rarity: Short = collection.propertyValues(EditionProperties.rarity).smallintValues.head
  val effect: Option[String] = collection.propertyValues.get(EditionProperties.effect).flatMap(_.textValues.headOption)
  val effectRaw: Option[String] = collection.propertyValues.get(EditionProperties.effectRaw).flatMap(_.textValues.headOption)
  val flavourText: Option[String] = collection.propertyValues.get(EditionProperties.flavourText).flatMap(_.textValues.headOption)
  val configuration: Option[String] = collection.propertyValues.get(EditionProperties.configuration).flatMap(_.textValues.headOption)
  val orientation: Option[String] = collection.propertyValues.get(EditionProperties.orientation).flatMap(_.textValues.headOption)

case class InnerCard
(
  collection: projected.Collection,
  innerEdition: InnerEdition,
  references: Seq[Reference],
  rules: Seq[Rule]
) extends projected.HasCollection, CardCommon:
  val name: String = collection.propertyValues(CoreProperties.name).textValues.head
  val cardUID: String = collection.propertyValues(CardProperties.cardUID).textValues.head
  val element: String = collection.propertyValues(CardProperties.element).textValues.head
  val types: Seq[String] = collection.propertyValues(CardProperties.types).textValues
  val classes: Seq[String] = collection.propertyValues(CardProperties.classes).textValues
  val subTypes: Seq[String] = collection.propertyValues(CardProperties.subTypes).textValues
  val effect: Option[String] = collection.propertyValues.get(CardProperties.effect).flatMap(_.textValues.headOption)
  val effectRaw: Option[String] = collection.propertyValues.get(CardProperties.effectRaw).flatMap(_.textValues.headOption)
  val memoryCost: Option[Short] = collection.propertyValues.get(CardProperties.memoryCost).flatMap(_.smallintValues.headOption)
  val reserveCost: Option[Short] = collection.propertyValues.get(CardProperties.reserveCost).flatMap(_.smallintValues.headOption)
  val level: Option[Short] = collection.propertyValues.get(CardProperties.level).flatMap(_.smallintValues.headOption)
  val speed: Option[String] = collection.propertyValues.get(CardProperties.speed).flatMap(_.textValues.headOption)
  val legality: Option[io.circe.Json] = collection.propertyValues.get(CardProperties.legality).flatMap(_.jsonValues.headOption)
  val power: Option[Short] = collection.propertyValues.get(CardProperties.power).flatMap(_.smallintValues.headOption)
  val life: Option[Short] = collection.propertyValues.get(CardProperties.life).flatMap(_.smallintValues.headOption)
  val durability: Option[Short] = collection.propertyValues.get(CardProperties.durability).flatMap(_.smallintValues.headOption)

case class Edition
(
  collection: projected.Collection,
  set: SetData,
  circulations: Seq[Circulation],
  innerCards: Seq[InnerCard]
) extends projected.HasCollection, EditionCommon:
  val editionUID: String = collection.propertyValues(EditionProperties.editionUID).textValues.head
  val cardUID: String = collection.propertyValues(EditionProperties.cardUID).textValues.head
  val name: Seq[String] = collection.propertyValues(CoreProperties.name).textValues
  val collectorNumber: String = collection.propertyValues(EditionProperties.collectorNumber).textValues.head
  val illustrator: Option[String] = collection.propertyValues.get(EditionProperties.illustrator).flatMap(_.textValues.headOption)
  val image: String = collection.propertyValues(EditionProperties.image).textValues.head
  val slug: String = collection.propertyValues(EditionProperties.slug).textValues.head
  val rarity: Short = collection.propertyValues(EditionProperties.rarity).smallintValues.head
  val effect: Option[String] = collection.propertyValues.get(EditionProperties.effect).flatMap(_.textValues.headOption)
  val effectRaw: Option[String] = collection.propertyValues.get(EditionProperties.effectRaw).flatMap(_.textValues.headOption)
  val flavourText: Option[String] = collection.propertyValues.get(EditionProperties.flavourText).flatMap(_.textValues.headOption)
  val configuration: Option[String] = collection.propertyValues.get(EditionProperties.configuration).flatMap(_.textValues.headOption)
  val orientation: Option[String] = collection.propertyValues.get(EditionProperties.orientation).flatMap(_.textValues.headOption)

case class CardData
(
  collection: projected.Collection,
  editions: Seq[Edition],
  references: Seq[Reference],
  rules: Seq[Rule]
) extends projected.HasCollection, CardCommon:
  val name: String = collection.propertyValues(CoreProperties.name).textValues.head
  val cardUID: String = collection.propertyValues(CardProperties.cardUID).textValues.head
  val element: String = collection.propertyValues(CardProperties.element).textValues.head
  val types: Seq[String] = collection.propertyValues(CardProperties.types).textValues
  val classes: Seq[String] = collection.propertyValues(CardProperties.classes).textValues
  val subTypes: Seq[String] = collection.propertyValues(CardProperties.subTypes).textValues
  val effect: Option[String] = collection.propertyValues.get(CardProperties.effect).flatMap(_.textValues.headOption)
  val effectRaw: Option[String] = collection.propertyValues.get(CardProperties.effectRaw).flatMap(_.textValues.headOption)
  val memoryCost: Option[Short] = collection.propertyValues.get(CardProperties.memoryCost).flatMap(_.smallintValues.headOption)
  val reserveCost: Option[Short] = collection.propertyValues.get(CardProperties.reserveCost).flatMap(_.smallintValues.headOption)
  val level: Option[Short] = collection.propertyValues.get(CardProperties.level).flatMap(_.smallintValues.headOption)
  val speed: Option[String] = collection.propertyValues.get(CardProperties.speed).flatMap(_.textValues.headOption)
  val legality: Option[io.circe.Json] = collection.propertyValues.get(CardProperties.legality).flatMap(_.jsonValues.headOption)
  val power: Option[Short] = collection.propertyValues.get(CardProperties.power).flatMap(_.smallintValues.headOption)
  val life: Option[Short] = collection.propertyValues.get(CardProperties.life).flatMap(_.smallintValues.headOption)
  val durability: Option[Short] = collection.propertyValues.get(CardProperties.durability).flatMap(_.smallintValues.headOption)

case class SetCard
(
  collection: projected.Collection,
  cardDataCollection: CardData
  // TODO: Primary edition ref?
  // TODO: Backref to set?
) extends projected.HasCollection:
  val collectorNumbesr: Seq[String] = collection.propertyValues(EditionProperties.collectorNumber).textValues

case class SetData
(
  collection: projected.Collection
) extends projected.HasCollection:
  val name: String = collection.propertyValues(CoreProperties.name).textValues.head
  val prefix: String = collection.propertyValues(SetProperties.prefix).textValues.head
  val language: String = collection.propertyValues(SetProperties.language).textValues.head

case class SetCollection
(
  collection: projected.Collection,
  setCardCollections: Seq[SetCard]
) extends projected.HasCollection

