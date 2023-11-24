package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.entity
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueTinyint
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Byte = Byte.MinValue,
) extends entity.PropertyValueTinyint

class PropertyValueTinyintSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueTinyint](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueTinyint])(rs: WrappedResultSet): PropertyValueTinyint =
    // TODO: Implement properly
    PropertyValueTinyint(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueTinyint extends PropertyValueTinyintSQLSyntaxSupport("PROPERTY_VALUE_TINYINT")
