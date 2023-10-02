package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

import scalikejdbc._

case class Collection
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
)

object Collection extends SQLSyntaxSupport[Collection]:
  override val schemaName = Some("public")
  override val tableName = "collection"

  def apply(c: ResultName[Collection])(rs: WrappedResultSet) =
    new Collection(
      pk = UUID.fromString(rs.string(c.pk)),
      virtual = rs.boolean(c.virtual),
      deleted = rs.boolean(c.deleted),
      created = rs.zonedDateTime(c.created),
      modified = rs.zonedDateTime(c.modified)
    )

val c1 = Collection.syntax("c1")
val c2 = Collection.syntax("c2")
