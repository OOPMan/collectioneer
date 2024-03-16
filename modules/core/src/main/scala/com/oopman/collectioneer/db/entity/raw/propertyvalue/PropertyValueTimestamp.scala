package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueTimestamp
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: ZonedDateTime = ZonedDateTime.now(),
) extends raw.PropertyValueTimestamp

class PropertyValueTimestampSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueTimestamp](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueTimestamp])(rs: WrappedResultSet): PropertyValueTimestamp =
    // TODO: Implement properly
    PropertyValueTimestamp(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueTimestamp extends PropertyValueTimestampSQLSyntaxSupport("PROPERTY_VALUE_TIMESTAMP")
