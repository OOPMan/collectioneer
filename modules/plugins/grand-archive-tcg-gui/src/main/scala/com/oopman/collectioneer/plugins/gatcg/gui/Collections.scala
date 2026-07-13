package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.properties.*

case class Reference
(collection: projected.Collection) extends projected.HasCollection:
  val kind: String = collection.propertyValues(ReferenceProperties.kind).stringValues.head
  val name: String = collection.propertyValues(CoreProperties.name).stringValues.head
  val slug: String = collection.propertyValues(ReferenceProperties.slug).stringValues.head
  val direction: String = collection.propertyValues(ReferenceProperties.direction).stringValues.head

case class Rule
(collection: projected.Collection) extends projected.HasCollection:
  val title: String = collection.propertyValues(CoreProperties.name).stringValues.head
  val description: String = collection.propertyValues(CoreProperties.description).stringValues.head
  val dateAdded: String = collection.propertyValues(RuleProperties.dateAdded).stringValues.head

case class Circulation
(collection: projected.Collection) extends projected.HasCollection:
  val name: Seq[String] = collection.propertyValues(CoreProperties.name).stringValues
  val foil: Boolean = collection.propertyValues(CirculationProperties.foil).booleanValues.head
  val population: Int = collection.propertyValues(CirculationProperties.population).intValues.head
  val populationOperator: String = collection.propertyValues(CirculationProperties.populationOperator).stringValues.head
  
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
  val flavourText: Option[String]
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
  val editionUID: String = collection.propertyValues(EditionProperties.editionUID).stringValues.head
  val cardUID: String = collection.propertyValues(EditionProperties.cardUID).stringValues.head
  val name: Seq[String] = collection.propertyValues(CoreProperties.name).stringValues
  val collectorNumber: String = collection.propertyValues(EditionProperties.collectorNumber).stringValues.head
  val illustrator: Option[String] = collection.propertyValues.get(EditionProperties.illustrator).flatMap(_.stringValues.headOption)
  val image: String = collection.propertyValues(EditionProperties.image).stringValues.head
  val slug: String = collection.propertyValues(EditionProperties.slug).stringValues.head
  val rarity: Short = collection.propertyValues(EditionProperties.rarity).shortValues.head
  val effect: Option[String] = collection.propertyValues.get(EditionProperties.effect).flatMap(_.stringValues.headOption)
  val effectRaw: Option[String] = collection.propertyValues.get(EditionProperties.effectRaw).flatMap(_.stringValues.headOption)
  val flavourText: Option[String] = collection.propertyValues.get(EditionProperties.flavourText).flatMap(_.stringValues.headOption)
  val configuration: Option[String] = collection.propertyValues.get(EditionProperties.configuration).flatMap(_.stringValues.headOption)
  val orientation: Option[String] = collection.propertyValues.get(EditionProperties.orientation).flatMap(_.stringValues.headOption)

case class InnerCard
(
  collection: projected.Collection,
  innerEdition: InnerEdition,
  references: Seq[Reference],
  rules: Seq[Rule]
) extends projected.HasCollection, CardCommon:
  val name: String = collection.propertyValues(CoreProperties.name).stringValues.head
  val cardUID: String = collection.propertyValues(CardProperties.cardUID).stringValues.head
  val element: String = collection.propertyValues(CardProperties.element).stringValues.head
  val types: Seq[String] = collection.propertyValues(CardProperties.types).stringValues
  val classes: Seq[String] = collection.propertyValues(CardProperties.classes).stringValues
  val subTypes: Seq[String] = collection.propertyValues(CardProperties.subTypes).stringValues
  val effect: Option[String] = collection.propertyValues.get(CardProperties.effect).flatMap(_.stringValues.headOption)
  val effectRaw: Option[String] = collection.propertyValues.get(CardProperties.effectRaw).flatMap(_.stringValues.headOption)
  val flavourText: Option[String] = collection.propertyValues.get(CardProperties.flavourText).flatMap(_.stringValues.headOption)
  val memoryCost: Option[Short] = collection.propertyValues.get(CardProperties.memoryCost).flatMap(_.shortValues.headOption)
  val reserveCost: Option[Short] = collection.propertyValues.get(CardProperties.reserveCost).flatMap(_.shortValues.headOption)
  val level: Option[Short] = collection.propertyValues.get(CardProperties.level).flatMap(_.shortValues.headOption)
  val speed: Option[String] = collection.propertyValues.get(CardProperties.speed).flatMap(_.booleanValues.headOption.map(if _ then "Fast" else "Slow"))
  val legality: Option[io.circe.Json] = collection.propertyValues.get(CardProperties.legality).flatMap(_.jsonValues.headOption)
  val power: Option[Short] = collection.propertyValues.get(CardProperties.power).flatMap(_.shortValues.headOption)
  val life: Option[Short] = collection.propertyValues.get(CardProperties.life).flatMap(_.shortValues.headOption)
  val durability: Option[Short] = collection.propertyValues.get(CardProperties.durability).flatMap(_.shortValues.headOption)

case class Edition
(
  collection: projected.Collection,
  set: SetData,
  circulations: Seq[Circulation],
  innerCards: Seq[InnerCard]
) extends projected.HasCollection, EditionCommon:
  val editionUID: String = collection.propertyValues(EditionProperties.editionUID).stringValues.head
  val cardUID: String = collection.propertyValues(EditionProperties.cardUID).stringValues.head
  val name: Seq[String] = collection.propertyValues(CoreProperties.name).stringValues
  val collectorNumber: String = collection.propertyValues(EditionProperties.collectorNumber).stringValues.head
  val illustrator: Option[String] = collection.propertyValues.get(EditionProperties.illustrator).flatMap(_.stringValues.headOption)
  val image: String = collection.propertyValues(EditionProperties.image).stringValues.head
  val slug: String = collection.propertyValues(EditionProperties.slug).stringValues.head
  val rarity: Short = collection.propertyValues(EditionProperties.rarity).shortValues.head
  val effect: Option[String] = collection.propertyValues.get(EditionProperties.effect).flatMap(_.stringValues.headOption)
  val effectRaw: Option[String] = collection.propertyValues.get(EditionProperties.effectRaw).flatMap(_.stringValues.headOption)
  val flavourText: Option[String] = collection.propertyValues.get(EditionProperties.flavourText).flatMap(_.stringValues.headOption)
  val configuration: Option[String] = collection.propertyValues.get(EditionProperties.configuration).flatMap(_.stringValues.headOption)
  val orientation: Option[String] = collection.propertyValues.get(EditionProperties.orientation).flatMap(_.stringValues.headOption)

case class CardData
(
  collection: projected.Collection,
  editions: Seq[Edition],
  references: Seq[Reference],
  rules: Seq[Rule]
) extends projected.HasCollection, CardCommon:
  val name: String = collection.propertyValues(CoreProperties.name).stringValues.head
  val cardUID: String = collection.propertyValues(CardProperties.cardUID).stringValues.head
  val element: String = collection.propertyValues(CardProperties.element).stringValues.head
  val types: Seq[String] = collection.propertyValues(CardProperties.types).stringValues
  val classes: Seq[String] = collection.propertyValues(CardProperties.classes).stringValues
  val subTypes: Seq[String] = collection.propertyValues(CardProperties.subTypes).stringValues
  val effect: Option[String] = collection.propertyValues.get(CardProperties.effect).flatMap(_.stringValues.headOption)
  val effectRaw: Option[String] = collection.propertyValues.get(CardProperties.effectRaw).flatMap(_.stringValues.headOption)
  val flavourText: Option[String] = collection.propertyValues.get(CardProperties.flavourText).flatMap(_.stringValues.headOption)
  val memoryCost: Option[Short] = collection.propertyValues.get(CardProperties.memoryCost).flatMap(_.shortValues.headOption)
  val reserveCost: Option[Short] = collection.propertyValues.get(CardProperties.reserveCost).flatMap(_.shortValues.headOption)
  val level: Option[Short] = collection.propertyValues.get(CardProperties.level).flatMap(_.shortValues.headOption)
  val speed: Option[String] = collection.propertyValues.get(CardProperties.speed).flatMap(_.booleanValues.headOption.map(if _ then "Fast" else "Slow"))
  val legality: Option[io.circe.Json] = collection.propertyValues.get(CardProperties.legality).flatMap(_.jsonValues.headOption)
  val power: Option[Short] = collection.propertyValues.get(CardProperties.power).flatMap(_.shortValues.headOption)
  val life: Option[Short] = collection.propertyValues.get(CardProperties.life).flatMap(_.shortValues.headOption)
  val durability: Option[Short] = collection.propertyValues.get(CardProperties.durability).flatMap(_.shortValues.headOption)

case class SetCard
(
  collection: projected.Collection,
  cardDataCollection: CardData
  // TODO: Primary edition ref?
  // TODO: Backref to set?
) extends projected.HasCollection:
  val collectorNumbesr: Seq[String] = collection.propertyValues(EditionProperties.collectorNumber).stringValues

case class SetData
(
  collection: projected.Collection
) extends projected.HasCollection:
  val name: String = collection.propertyValues(CoreProperties.name).stringValues.head
  val prefix: String = collection.propertyValues(SetProperties.prefix).stringValues.head
  val language: String = collection.propertyValues(SetProperties.language).stringValues.head

case class SetCollection
(
  collection: projected.Collection,
  setCardCollections: Seq[SetCard]
) extends projected.HasCollection

