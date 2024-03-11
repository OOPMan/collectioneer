package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueSet
(
  pk: UUID = UUID.randomUUID(),
  created: ZonedDateTime = ZonedDateTime.now(),
  properties: Seq[Property] = Nil,
  propertyValues: Seq[PropertyValue] = Nil
) extends traits.entity.projected.PropertyValueSet
