package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueFloatSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueFloat](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueFloat])(rs: WrappedResultSet): raw.PropertyValueFloat =
    // TODO: Implement properly
    raw.PropertyValueFloat(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueFloat extends PropertyValueFloatSQLSyntaxSupport("PROPERTY_VALUE_FLOAT")
