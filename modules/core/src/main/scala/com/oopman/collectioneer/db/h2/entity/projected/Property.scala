package com.oopman.collectioneer.db.h2.entity.projected

import com.oopman.collectioneer.db.entity.Utils.resultSetArrayToPropertyTypeList
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String = "",
  propertyTypes: List[PropertyType] = Nil,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValues: List[PropertyValue] = Nil
) extends traits.entity.projected.Property

object Property extends SQLSyntaxSupport[Property]:
  override val schemaName = Some("public")
  override val tableName = "property"

  def apply(p: ResultName[Property], propertyValues: List[PropertyValue])(rs: WrappedResultSet) =
    val propertyType = resultSetArrayToPropertyTypeList(rs, p.propertyTypes)
    new Property(
      pk = UUID.fromString(rs.string(p.pk)),
      propertyName = rs.string(p.propertyName),
      propertyTypes = propertyType,
      deleted = rs.boolean(p.deleted),
      created = rs.zonedDateTime(p.created),
      modified = rs.zonedDateTime(p.modified),
      propertyValues = propertyValues
    )

val p1 = Property.syntax("p1")
val p2 = Property.syntax("p2")