package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.{OffsetTime, ZonedDateTime}
import java.util.UUID

case class PropertyValueTime
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: OffsetTime = OffsetTime.now(),
) extends raw.PropertyValueTime

class PropertyValueTimeSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueTime](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueTime])(rs: WrappedResultSet): PropertyValueTime =
    // TODO: Implement properly
    PropertyValueTime(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueTime extends PropertyValueTimeSQLSyntaxSupport("PROPERTY_VALUE_TIME")
