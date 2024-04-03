package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueNumericSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueBigDecimal](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueBigDecimal])(rs: WrappedResultSet): raw.PropertyValueBigDecimal =
    // TODO: Implement properly
    raw.PropertyValueBigDecimal(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueBigDecimal extends PropertyValueNumericSQLSyntaxSupport("PROPERTY_VALUE_NUMERIC")
