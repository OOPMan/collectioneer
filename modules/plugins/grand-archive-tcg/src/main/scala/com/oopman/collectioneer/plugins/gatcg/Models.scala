package com.oopman.collectioneer.plugins.gatcg


sealed trait GATCG

case class Card
(
  uuid: String,
  types: List[String],
  classes: List[String],
  subtypes: List[String],
  element: String,
  name: String,
  effect_raw: Option[String],
  rule: Option[List[io.circe.Json]],
  flavor: Option[String],
  cost_memory: Option[Int],
  cost_reserve: Option[Int],
  level: Option[Int],
  power: Option[Int],
  life: Option[Int],
  durability: Option[Int],
  speed: Option[Boolean],
  legality: Option[io.circe.Json],
  editions: List[Edition]
) extends GATCG

case class Edition
(
  uuid: String,
  card_id: String,
  collector_number: String,
  slug: String,
  illustrator: String,
  rarity: Int,
  effect: Option[String],
  flavor: Option[String],
  circulations: List[Circulation],
  circulationTemplates: List[Circulation],
  set: Set
) extends GATCG

case class Circulation
(
  uuid: String,
  name: Option[String],
  foil: Option[Boolean],
  population: Int
) extends GATCG

case class Set
(
  name: String,
  prefix: String,
  language: String
) extends GATCG

object Models:
  import io.circe.Decoder
  import io.circe.generic.semiauto.*
  implicit val setDecoder: Decoder[Set] = deriveDecoder[Set]
  implicit val circulationDecoder: Decoder[Circulation] = deriveDecoder[Circulation]
  implicit val editionDecoder: Decoder[Edition] = deriveDecoder[Edition]
  implicit val cardDecoder: Decoder[Card] = deriveDecoder[Card]