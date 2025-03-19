package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyType:
  case text extends PropertyType
  case bytes extends PropertyType
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
  case uuid extends PropertyType
  case json extends PropertyType

trait Property:
  val pk: UUID
  val propertyName: String
  val propertyTypes: Seq[PropertyType]
  val deleted: Boolean
  val created: ZonedDateTime
  val modified: ZonedDateTime


