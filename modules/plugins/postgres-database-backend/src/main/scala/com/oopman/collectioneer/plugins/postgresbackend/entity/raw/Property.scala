package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

object Property extends SQLSyntaxSupport[raw.Property]:
  override val schemaName = Some("public")
  override val tableName = "property"
  val p1 = Property.syntax("p1")
  val p2 = Property.syntax("p2")

  def apply(p: ResultName[raw.Property])(rs: WrappedResultSet) =
    val propertyTypes = rs
      .array(p.propertyTypes)
      .getArray()
      .asInstanceOf[Array[Object]]
      .map(s => PropertyType.valueOf(s.asInstanceOf[String]))
      .toList
    raw.Property(
      pk = UUID.fromString(rs.string(p.pk)),
      propertyName = rs.string(p.propertyName),
      propertyTypes = propertyTypes,
      deleted = rs.boolean(p.deleted),
      created = rs.zonedDateTime(p.created),
      modified = rs.zonedDateTime(p.modified)
    )
