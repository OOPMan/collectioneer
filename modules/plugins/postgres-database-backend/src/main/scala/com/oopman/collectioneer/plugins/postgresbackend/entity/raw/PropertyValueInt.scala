package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueIntSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueInt](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueInt])(rs: WrappedResultSet): raw.PropertyValueInt =
    // TODO: Implement properly
    raw.PropertyValueInt(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueInt extends PropertyValueIntSQLSyntaxSupport("PROPERTY_VALUE_INT")
