package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String,
  propertyTypes: List[traits.entity.raw.PropertyType],
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
) extends traits.entity.raw.Property

object Property extends SQLSyntaxSupport[Property]:
  override val schemaName = Some("public")
  override val tableName = "property"
  val p1 = Property.syntax("p1")
  val p2 = Property.syntax("p2")

  def apply(p: ResultName[Property])(rs: WrappedResultSet) =
    val propertyType = rs
      .array(p.propertyTypes)
      .getArray()
      .asInstanceOf[Array[Object]]
      .map(s => traits.entity.raw.PropertyType.valueOf(s.asInstanceOf[String]))
      .toList
    new Property(
      pk = UUID.fromString(rs.string(p.pk)),
      propertyName = rs.string(p.propertyName),
      propertyTypes = propertyType,
      deleted = rs.boolean(p.deleted),
      created = rs.zonedDateTime(p.created),
      modified = rs.zonedDateTime(p.modified)
    )
