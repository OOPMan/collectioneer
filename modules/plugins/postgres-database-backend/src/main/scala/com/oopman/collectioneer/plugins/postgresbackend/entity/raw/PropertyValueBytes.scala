package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueVarbinarySQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueBytes](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueBytes])(rs: WrappedResultSet): raw.PropertyValueBytes =
    // TODO: Implement properly
    raw.PropertyValueBytes(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueBytes extends PropertyValueVarbinarySQLSyntaxSupport("PROPERTY_VALUE_VARBINARY")
