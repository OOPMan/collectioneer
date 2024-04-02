package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import scalikejdbc.*

import java.util.UUID

object Collection extends SQLSyntaxSupport[raw.Collection]:
  override val schemaName = Some("public")
  override val tableName = "collection"
  val c1 = Collection.syntax("c1")
  val c2 = Collection.syntax("c2")
  
  def apply(c: ResultName[raw.Collection])(rs: WrappedResultSet) =
    raw.Collection(
      pk = UUID.fromString(rs.string(c.pk)),
      virtual = rs.boolean(c.virtual),
      deleted = rs.boolean(c.deleted),
      created = rs.zonedDateTime(c.created),
      modified = rs.zonedDateTime(c.modified)
    )

