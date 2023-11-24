package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.entity
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueBigint
(
  pk: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: BigInt = BigInt.int2bigInt(Int.MinValue),
) extends entity.PropertyValueBigint

class PropertyValueBigintSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueBigint](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueBigint])(rs: WrappedResultSet): PropertyValueBigint =
    // TODO: Implement properly
    PropertyValueBigint(
      propertyValueSetPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueBigint extends PropertyValueBigintSQLSyntaxSupport("PROPERTY_VALUE_BIGINT")
