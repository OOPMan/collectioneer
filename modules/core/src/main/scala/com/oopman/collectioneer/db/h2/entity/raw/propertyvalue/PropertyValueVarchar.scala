package com.oopman.collectioneer.db.h2.entity.raw.propertyvalue

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.h2.queries.raw.PropertyValueQueries
import com.oopman.collectioneer.db.traits.entity.raw
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueVarchar
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: String = "",
) extends raw.PropertyValueVarchar

class PropertyValueVarcharSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueVarchar](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueVarchar])(rs: WrappedResultSet): PropertyValueVarchar =
    // TODO: Implement properly
    PropertyValueVarchar(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueVarchar extends PropertyValueVarcharSQLSyntaxSupport("PROPERTY_VALUE_VARCHAR")