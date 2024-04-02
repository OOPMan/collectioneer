package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueTimestampSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueTimestamp](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueTimestamp])(rs: WrappedResultSet): raw.PropertyValueTimestamp =
    // TODO: Implement properly
    raw.PropertyValueTimestamp(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueTimestamp extends PropertyValueTimestampSQLSyntaxSupport("PROPERTY_VALUE_TIMESTAMP")
