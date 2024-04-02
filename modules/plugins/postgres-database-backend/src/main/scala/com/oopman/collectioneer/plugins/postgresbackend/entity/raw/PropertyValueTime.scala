package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.{OffsetTime, ZonedDateTime}
import java.util.UUID

class PropertyValueTimeSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueTime](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueTime])(rs: WrappedResultSet): raw.PropertyValueTime =
    // TODO: Implement properly
    raw.PropertyValueTime(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueTime extends PropertyValueTimeSQLSyntaxSupport("PROPERTY_VALUE_TIME")
