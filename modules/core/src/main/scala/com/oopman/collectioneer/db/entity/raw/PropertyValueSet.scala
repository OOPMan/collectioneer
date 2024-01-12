package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class PropertyValueSet
(
  pk: UUID = UUID.randomUUID(),
  created: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.PropertyValueSet

object PropertyValueSet extends SQLSyntaxSupport[PropertyValueSet]:
  override val schemaName = Some("public")
  override val tableName = "property_value_set"

  def apply(pvc: ResultName[PropertyValueSet])(rs: WrappedResultSet) =
    new PropertyValueSet(
      pk = UUID.fromString(rs.string(pvc.pk)),
      created = rs.zonedDateTime(pvc.created)
    )

val pvc1 = PropertyValueSet.syntax("pvc1")
