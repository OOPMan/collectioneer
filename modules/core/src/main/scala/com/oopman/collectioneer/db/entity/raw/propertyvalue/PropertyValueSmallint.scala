package com.oopman.collectioneer.db.entity.raw.propertyvalue

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueSmallint
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  propertyPK: UUID,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValue: Short = Short.MinValue,
) extends raw.PropertyValueSmallint

class PropertyValueSmallintSQLSyntaxSupport(override val tableName: String) extends PropertyValueSQLSyntaxSupport[PropertyValueSmallint](tableName):
  override def apply(pv: scalikejdbc.ResultName[PropertyValueSmallint])(rs: WrappedResultSet): PropertyValueSmallint =
    // TODO: Implement properly
    PropertyValueSmallint(
      collectionPK = UUID.randomUUID(),
      propertyPK = UUID.randomUUID(),
    )

object PropertyValueSmallint extends PropertyValueSmallintSQLSyntaxSupport("PROPERTY_VALUE_SMALLINT")
