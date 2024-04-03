package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueBooleanSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueBoolean](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueBoolean])(rs: WrappedResultSet): raw.PropertyValueBoolean =
    // TODO: Implement properly
    raw.PropertyValueBoolean(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueBoolean extends PropertyValueBooleanSQLSyntaxSupport("PROPERTY_VALUE_BOOLEAN")
