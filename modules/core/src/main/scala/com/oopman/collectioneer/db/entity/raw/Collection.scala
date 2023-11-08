package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.entity

import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class Collection
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
) extends entity.Collection

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
