package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueVarcharSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueText](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueText])(rs: WrappedResultSet): raw.PropertyValueText =
    // TODO: Implement properly
    raw.PropertyValueText(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueText extends PropertyValueVarcharSQLSyntaxSupport("PROPERTY_VALUE_VARCHAR")