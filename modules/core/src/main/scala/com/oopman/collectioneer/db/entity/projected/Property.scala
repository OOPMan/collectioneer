package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity.Utils.resultSetArrayToPropertyTypeList
import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID
import scalikejdbc.*

case class Property
(
  pk: UUID = UUID.randomUUID(),
  propertyName: String = "",
  propertyTypes: List[traits.entity.raw.PropertyType] = Nil,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
  propertyValues: List[PropertyValue] = Nil,
  propertyValueSets: List[PropertyValueSet] = Nil
) extends traits.entity.projected.Property

object Property extends SQLSyntaxSupport[Property]:
  override val schemaName = Some("public")
  override val tableName = "property"
  val p1 = Property.syntax("p1")
  val p2 = Property.syntax("p2")

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
