package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueUUIDSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueUUID](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueUUID])(rs: WrappedResultSet): raw.PropertyValueUUID =
    // TODO: Implement properly
    raw.PropertyValueUUID(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueUUID extends PropertyValueUUIDSQLSyntaxSupport("PROPERTY_VALUE_UUID")
