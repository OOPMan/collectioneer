package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyType:
  case varchar extends PropertyType
  case varbinary extends PropertyType
  case tinyint extends PropertyType
  case smallint extends PropertyType
  case int extends PropertyType
  case bigint extends PropertyType
  case numeric extends PropertyType
  case float extends PropertyType
  case double extends PropertyType
  case boolean extends PropertyType
  case date extends PropertyType
  case time extends PropertyType
  case timestamp extends PropertyType
  case clob extends PropertyType
  case blob extends PropertyType
  case uuid extends PropertyType
  case json extends PropertyType

trait Property:
  val pk: UUID
  val propertyName: String
  val propertyTypes: List[PropertyType]
  val deleted: Boolean
  val created: ZonedDateTime
  val modified: ZonedDateTime

object Property:
  def propertiesSeqToBatchInsertSeq(properties: Seq[Property]): Seq[Seq[Any]] =
    properties.map(p => Seq(
      p.pk.toString,
      p.propertyName,
      p.propertyTypes.map(_.toString).toArray,
      p.deleted
    ))

