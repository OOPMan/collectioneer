package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

import scalikejdbc._

case class PropertyValueSets
(
  pk: UUID = UUID.randomUUID(),
  created: ZonedDateTime = ZonedDateTime.now()
)

object PropertyValueSets extends SQLSyntaxSupport[PropertyValueSets]:
  override val schemaName = Some("public")
  override val tableName = "property_value_sets"

  def apply(pvc: ResultName[PropertyValueSets])(rs: WrappedResultSet) =
    new PropertyValueSets(
      pk = UUID.fromString(rs.string(pvc.pk)),
      created = rs.zonedDateTime(pvc.created)
    )

val pvc1 = PropertyValueSets.syntax("pvc1")
