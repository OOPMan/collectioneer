package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueJSONSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueJSON](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueJSON])(rs: WrappedResultSet): raw.PropertyValueJSON =
    // TODO: Implement properly
    raw.PropertyValueJSON(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueJSON extends PropertyValueJSONSQLSyntaxSupport("PROPERTY_VALUE_JSON")
