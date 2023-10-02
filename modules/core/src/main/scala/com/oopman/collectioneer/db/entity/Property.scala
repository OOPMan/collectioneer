package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

import scalikejdbc._

enum PropertyType:
  case varchar extends PropertyType
  case varbinary extends PropertyType
  case tinyint extends PropertyType
  case smallint extends PropertyType
  case int extends PropertyType
  case bigint extends PropertyType
  case numeric extends PropertyType
  case float extends PropertyType
  case double extends PropertyType
  case boolean extends PropertyType
  case date extends PropertyType
  case time extends PropertyType
  case timestamp extends PropertyType
  case clob extends PropertyType
  case blob extends PropertyType
  case uuid extends PropertyType
  case json extends PropertyType

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String,
  propertyTypes: List[PropertyType],
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
)

object Property extends SQLSyntaxSupport[Property]:
  override val schemaName = Some("public")
  override val tableName = "property"

  def apply(p: ResultName[Property])(rs: WrappedResultSet) =
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
      modified = rs.zonedDateTime(p.modified)
    )

val p1 = Property.syntax("p1")
val p2 = Property.syntax("p2")