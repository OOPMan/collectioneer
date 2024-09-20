package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import scalikejdbc.*

import java.util.UUID

object Property extends SQLSyntaxSupport[raw.Property]:
  override val schemaName = Some("public")
  override val tableName = "property"
  val p1 = Property.syntax("p1")
  val p2 = Property.syntax("p2")

  def apply(p: ResultName[raw.Property])(rs: WrappedResultSet): raw.Property =
    apply(rs, p.pk, p.propertyName, p.propertyTypes, p.deleted, p.created, p.modified)

  def apply(rs: WrappedResultSet,
            pkColumn: String = "pk",
            propertyNameColumn: String = "property_name",
            propertyTypesColumn: String = "property_types",
            deletedColumn: String = "deleted",
            createdColumn: String = "created",
            modifiedColmn: String = "modified"): raw.Property =
    val propertyTypes = rs
      .array(propertyTypesColumn)
      .getArray()
      .asInstanceOf[Array[Object]]
      .map(s => PropertyType.valueOf(s.asInstanceOf[String]))
      .toList
    raw.Property(
      pk = UUID.fromString(rs.string(pkColumn)),
      propertyName = rs.string(propertyNameColumn),
      propertyTypes = propertyTypes,
      deleted = rs.boolean(deletedColumn),
      created = rs.zonedDateTime(createdColumn),
      modified = rs.zonedDateTime(modifiedColmn)
    )

  def propertiesSeqToBatchInsertSeq(properties: Seq[traits.entity.raw.Property]): Seq[Seq[Any]] =
    properties.map(p => Seq(
      p.pk,
      p.propertyName,
      p.propertyTypes.map(_.toString).toArray,
      p.deleted
    ))