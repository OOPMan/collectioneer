package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.entity
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueInt
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Int = Int.MinValue,
) extends entity.PropertyValueInt

class PropertyValueIntSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueInt](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueInt])(rs: WrappedResultSet): PropertyValueInt =
    // TODO: Implement properly
    PropertyValueInt(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueInt extends PropertyValueIntSQLSyntaxSupport("PROPERTY_VALUE_INT")
