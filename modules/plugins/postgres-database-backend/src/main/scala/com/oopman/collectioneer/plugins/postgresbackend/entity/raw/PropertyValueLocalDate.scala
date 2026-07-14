package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.util.UUID

class PropertyValueDateSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueLocalDate](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueLocalDate])(rs: WrappedResultSet): raw.PropertyValueLocalDate =
    // TODO: Implement properly
    raw.PropertyValueLocalDate(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueLocalDate extends PropertyValueDateSQLSyntaxSupport("PROPERTY_VALUE_LOCALDATE")
