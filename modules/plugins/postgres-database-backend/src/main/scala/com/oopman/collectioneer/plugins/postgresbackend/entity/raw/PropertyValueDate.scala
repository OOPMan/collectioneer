package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.time.{LocalDate, ZonedDateTime}
import java.util.UUID

class PropertyValueDateSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueDate](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueDate])(rs: WrappedResultSet): raw.PropertyValueDate =
    // TODO: Implement properly
    raw.PropertyValueDate(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueDate extends PropertyValueDateSQLSyntaxSupport("PROPERTY_VALUE_DATE")
