package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.sql.Blob
import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueBLOB
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Blob,
) extends raw.PropertyValueBLOB

class PropertyValueBLOBSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueBLOB](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueBLOB])(rs: WrappedResultSet): PropertyValueBLOB =
    // TODO: Implement properly
    PropertyValueBLOB(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
      propertyValue = rs.blob(0)
    )

object PropertyValueBLOB extends PropertyValueBLOBSQLSyntaxSupport("PROPERTY_VALUE_BLOB")
