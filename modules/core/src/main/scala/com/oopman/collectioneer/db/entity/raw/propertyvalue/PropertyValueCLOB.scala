package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.sql.Clob
import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueCLOB
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Clob,
) extends traits.entity.PropertyValueCLOB

class PropertyValueCLOBSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueCLOB](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueCLOB])(rs: WrappedResultSet): PropertyValueCLOB =
    // TODO: Implement properly
    PropertyValueCLOB(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
      propertyValue = rs.clob(0)
    )

object PropertyValueCLOB extends PropertyValueCLOBSQLSyntaxSupport("PROPERTY_VALUE_CLOB")
