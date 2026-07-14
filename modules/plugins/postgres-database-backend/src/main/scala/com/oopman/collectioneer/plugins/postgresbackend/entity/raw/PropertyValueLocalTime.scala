package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.util.UUID

class PropertyValueTimeSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueLocalTime](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueLocalTime])(rs: WrappedResultSet): raw.PropertyValueLocalTime =
    // TODO: Implement properly
    raw.PropertyValueLocalTime(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueLocalTime extends PropertyValueTimeSQLSyntaxSupport("PROPERTY_VALUE_LOCALTIME")
