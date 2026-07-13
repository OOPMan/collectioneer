package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.util.UUID

class PropertyValueLongSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueLong](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueLong])(rs: WrappedResultSet): raw.PropertyValueLong =
    // TODO: Implement properly
    raw.PropertyValueLong(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueLong extends PropertyValueLongSQLSyntaxSupport("PROPERTY_VALUE_LONG")
