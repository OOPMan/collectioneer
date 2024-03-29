package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueNumeric
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: BigDecimal = BigDecimal.valueOf(Double.MinValue),
) extends raw.PropertyValueNumeric

class PropertyValueNumericSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueNumeric](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueNumeric])(rs: WrappedResultSet): PropertyValueNumeric =
    // TODO: Implement properly
    PropertyValueNumeric(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueNumeric extends PropertyValueNumericSQLSyntaxSupport("PROPERTY_VALUE_NUMERIC")
