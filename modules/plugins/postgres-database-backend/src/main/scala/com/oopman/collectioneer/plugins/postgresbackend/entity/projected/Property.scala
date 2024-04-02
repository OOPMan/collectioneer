package com.oopman.collectioneer.plugins.postgresbackend.entity.projected

import com.oopman.collectioneer.db.entity.Utils.resultSetArrayToPropertyTypeList
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

import java.time.ZonedDateTime
import java.util.UUID

object Property extends SQLSyntaxSupport[entity.projected.Property]:
  override val schemaName = Some("public")
  override val tableName = "property"
  val p1 = Property.syntax("p1")
  val p2 = Property.syntax("p2")

  def apply(p: ResultName[entity.projected.Property], propertyValues: List[entity.projected.PropertyValue], collections: List[entity.projected.Collection])(rs: WrappedResultSet) =
    val propertyType = resultSetArrayToPropertyTypeList(rs, p.propertyTypes)
    entity.projected.Property(
      pk = UUID.fromString(rs.string(p.pk)),
      propertyName = rs.string(p.propertyName),
      propertyTypes = propertyType,
      deleted = rs.boolean(p.deleted),
      created = rs.zonedDateTime(p.created),
      modified = rs.zonedDateTime(p.modified),
      propertyValues = propertyValues,
    )
