package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

import scalikejdbc._

case class Collections
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
)

object Collections extends SQLSyntaxSupport[Collections]:
  override val schemaName = Some("public")
  override val tableName = "collections"

  def apply(c: ResultName[Collections])(rs: WrappedResultSet) =
    new Collections(
      pk = UUID.fromString(rs.string(c.pk)),
      virtual = rs.boolean(c.virtual),
      deleted = rs.boolean(c.deleted),
      created = rs.zonedDateTime(c.created),
      modified = rs.zonedDateTime(c.modified)
    )

val c = Collections.syntax("c")
