package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueVarcharSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueString](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueString])(rs: WrappedResultSet): raw.PropertyValueString =
    // TODO: Implement properly
    raw.PropertyValueString(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueText extends PropertyValueVarcharSQLSyntaxSupport("property_value_text")