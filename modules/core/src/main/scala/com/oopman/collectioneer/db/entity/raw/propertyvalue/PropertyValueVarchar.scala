package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.entity

import scalikejdbc._
import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueVarchar
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: String,
) extends entity.PropertyValueVarchar

class PropertyValueVarcharSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueVarchar](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueVarchar])(rs: WrappedResultSet): PropertyValueVarchar =
    // TODO: Implement properly
    PropertyValueVarchar(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
      propertyValue = ""
    )

object PropertyValueVarchar extends PropertyValueVarcharSQLSyntaxSupport("PROPERTY_VALUE_VARCHAR")
