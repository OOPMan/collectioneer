package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueBoolean
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Boolean = false,
) extends raw.PropertyValueBoolean

class PropertyValueBooleanSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueBoolean](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueBoolean])(rs: WrappedResultSet): PropertyValueBoolean =
    // TODO: Implement properly
    PropertyValueBoolean(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueBoolean extends PropertyValueBooleanSQLSyntaxSupport("PROPERTY_VALUE_BOOLEAN")
