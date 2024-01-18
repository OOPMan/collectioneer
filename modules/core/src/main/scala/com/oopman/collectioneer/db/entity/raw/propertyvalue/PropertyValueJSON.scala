package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueJSON
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Array[Byte] = Array.empty,
) extends raw.PropertyValueJSON

class PropertyValueJSONSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueJSON](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueJSON])(rs: WrappedResultSet): PropertyValueJSON =
    // TODO: Implement properly
    PropertyValueJSON(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueJSON extends PropertyValueJSONSQLSyntaxSupport("PROPERTY_VALUE_JSON")
