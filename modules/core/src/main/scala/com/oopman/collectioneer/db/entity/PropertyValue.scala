package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

trait PropertyValue:
  val pk: UUID
  val propertyValueSetPK: UUID
  val propertyPK: UUID
  val index: Int
  val created: ZonedDateTime
  val modified: ZonedDateTime

trait PropertyValueVarchar extends PropertyValue:
  val propertyValue: String

