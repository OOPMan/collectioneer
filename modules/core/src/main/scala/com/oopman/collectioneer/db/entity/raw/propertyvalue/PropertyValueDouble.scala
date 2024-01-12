package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueDouble
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Double = Double.MinValue,
) extends traits.entity.PropertyValueDouble

class PropertyValueDoubleSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueDouble](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueDouble])(rs: WrappedResultSet): PropertyValueDouble =
    // TODO: Implement properly
    PropertyValueDouble(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueDouble extends PropertyValueDoubleSQLSyntaxSupport("PROPERTY_VALUE_DOUBLE")
