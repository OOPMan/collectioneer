package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.scalikejdbc.entity.raw.PropertyValueSQLSyntaxSupport
import scalikejdbc.*

import java.util.UUID

class PropertyValueTimestampSQLSyntaxSupport(override val tableName: String)
extends PropertyValueSQLSyntaxSupport[raw.PropertyValueOffsetDateTime](tableName):
  override def apply(pv: scalikejdbc.ResultName[raw.PropertyValueOffsetDateTime])(rs: WrappedResultSet): raw.PropertyValueOffsetDateTime =
    // TODO: Implement properly
    raw.PropertyValueOffsetDateTime(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueOffsetDateTime extends PropertyValueTimestampSQLSyntaxSupport("PROPERTY_VALUE_OFFSETDATETIME")
