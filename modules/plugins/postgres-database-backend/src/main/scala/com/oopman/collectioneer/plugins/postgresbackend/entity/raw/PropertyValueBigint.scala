package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueBigintSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueBigInt](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueBigInt])(rs: WrappedResultSet): raw.PropertyValueBigInt =
    // TODO: Implement properly
    raw.PropertyValueBigInt(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueBigint extends PropertyValueBigintSQLSyntaxSupport("PROPERTY_VALUE_BIGINT")
