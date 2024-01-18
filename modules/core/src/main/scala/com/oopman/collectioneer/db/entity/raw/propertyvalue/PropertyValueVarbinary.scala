package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueVarbinary
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Array[Byte] = Array.empty,
) extends raw.PropertyValueVarbinary

class PropertyValueVarbinarySQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueVarbinary](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueVarbinary])(rs: WrappedResultSet): PropertyValueVarbinary =
    // TODO: Implement properly
    PropertyValueVarbinary(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueVarbinary extends PropertyValueVarbinarySQLSyntaxSupport("PROPERTY_VALUE_VARBINARY")
