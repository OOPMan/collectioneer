package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueFloat
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Float = Float.MinValue,
) extends traits.entity.PropertyValueFloat

class PropertyValueFloatSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueFloat](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueFloat])(rs: WrappedResultSet): PropertyValueFloat =
    // TODO: Implement properly
    PropertyValueFloat(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueFloat extends PropertyValueFloatSQLSyntaxSupport("PROPERTY_VALUE_FLOAT")
