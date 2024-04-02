package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

class PropertyValueSmallintSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueSmallint](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueSmallint])(rs: WrappedResultSet): raw.PropertyValueSmallint =
    // TODO: Implement properly
    raw.PropertyValueSmallint(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueSmallint extends PropertyValueSmallintSQLSyntaxSupport("PROPERTY_VALUE_SMALLINT")
