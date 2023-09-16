package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyTypes:
  case varchar extends PropertyTypes
  case varbinary extends PropertyTypes
  case tinyint extends PropertyTypes
  case smallint extends PropertyTypes
  case int extends PropertyTypes
  case bigint extends PropertyTypes
  case numeric extends PropertyTypes
  case float extends PropertyTypes
  case double extends PropertyTypes
  case boolean extends PropertyTypes
  case date extends PropertyTypes
  case time extends PropertyTypes
  case timestamp extends PropertyTypes
  case clob extends PropertyTypes
  case blob extends PropertyTypes
  case uuid extends PropertyTypes
  case json extends PropertyTypes

case class Properties
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String,
  propertyType: List[PropertyTypes],
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
)