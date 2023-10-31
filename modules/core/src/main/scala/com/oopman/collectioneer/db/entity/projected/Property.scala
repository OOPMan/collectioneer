package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity.{Property, PropertyType}
import scalikejdbc._

import java.time.ZonedDateTime
import java.util.UUID

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String,
  propertyTypes: List[PropertyType],
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  properties: List[Property] = Nil
)

object Property extends SQLSyntaxSupport[Property]:
  override val schemaName = Some("public")
  override val tableName = "property"

  def apply(p: ResultName[Property], properties: List[Property])(rs: WrappedResultSet) =
    val propertyType = rs
      .array(p.propertyTypes)
      .getArray()
      .asInstanceOf[Array[Object]]
      .map(s => PropertyType.valueOf(s.asInstanceOf[String]))
      .toList
    new Property(
      pk = UUID.fromString(rs.string(p.pk)),
      propertyName = rs.string(p.propertyName),
      propertyTypes = propertyType,
      deleted = rs.boolean(p.deleted),
      created = rs.zonedDateTime(p.created),
      modified = rs.zonedDateTime(p.modified),
      properties = properties
    )

val p1 = Property.syntax("p1")
val p2 = Property.syntax("p2")