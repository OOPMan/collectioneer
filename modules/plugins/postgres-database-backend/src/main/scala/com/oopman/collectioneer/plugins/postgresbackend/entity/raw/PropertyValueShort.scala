package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.util.UUID

class PropertyValueShortSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueShort](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueShort])(rs: WrappedResultSet): raw.PropertyValueShort =
    // TODO: Implement properly
    raw.PropertyValueShort(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueShort extends PropertyValueShortSQLSyntaxSupport("PROPERTY_VALUE_SHORT")
