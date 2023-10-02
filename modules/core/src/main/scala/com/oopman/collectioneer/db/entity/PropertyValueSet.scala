package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

import scalikejdbc._

case class PropertyValueSet
(
  pk: UUID = UUID.randomUUID(),
  created: ZonedDateTime = ZonedDateTime.now()
)

object PropertyValueSet extends SQLSyntaxSupport[PropertyValueSet]:
  override val schemaName = Some("public")
  override val tableName = "property_value_set"

  def apply(pvc: ResultName[PropertyValueSet])(rs: WrappedResultSet) =
    new PropertyValueSet(
      pk = UUID.fromString(rs.string(pvc.pk)),
      created = rs.zonedDateTime(pvc.created)
    )

val pvc1 = PropertyValueSet.syntax("pvc1")
