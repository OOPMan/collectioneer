package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.entity
import scalikejdbc.*

import java.time.{LocalDate, ZonedDateTime}
import java.util.UUID

case class PropertyValueDate
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: LocalDate = LocalDate.now(),
) extends entity.PropertyValueDate

class PropertyValueDateSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueDate](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueDate])(rs: WrappedResultSet): PropertyValueDate =
    // TODO: Implement properly
    PropertyValueDate(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueDate extends PropertyValueDateSQLSyntaxSupport("PROPERTY_VALUE_DATE")
