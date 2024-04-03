package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueBigintSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueBigint](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueBigint])(rs: WrappedResultSet): raw.PropertyValueBigint =
    // TODO: Implement properly
    raw.PropertyValueBigint(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueBigint extends PropertyValueBigintSQLSyntaxSupport("PROPERTY_VALUE_BIGINT")
