package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueDoubleSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueDouble](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueDouble])(rs: WrappedResultSet): raw.PropertyValueDouble =
    // TODO: Implement properly
    raw.PropertyValueDouble(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueDouble extends PropertyValueDoubleSQLSyntaxSupport("PROPERTY_VALUE_DOUBLE")
