package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueUUID
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: UUID = UUID.randomUUID(),
) extends raw.PropertyValueUUID

class PropertyValueUUIDSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueUUID](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueUUID])(rs: WrappedResultSet): PropertyValueUUID =
    // TODO: Implement properly
    PropertyValueUUID(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueUUID extends PropertyValueUUIDSQLSyntaxSupport("PROPERTY_VALUE_UUID")
