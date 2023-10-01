package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity.PropertyTypes
import scalikejdbc.*

import java.util.UUID

/*
case class PropertyValueVarchars
(
  propertyValueSetPk: UUID,
  propertyPk: UUID,
  pvcValues: Option[List[String]],
  pviValues: Option[List[Int]]
)

case class PropertyValueInts
(
  propertyValueSetPk: UUID,
  propertyPk: UUID,
  pvcValues: Option[List[String]],
  pviValues: Option[List[Int]]
)
*
 */

case class PropertyValueInner
(
  propertyValueSetPk: UUID,
  propertyPk: UUID,
  pvcValues: Option[List[String]],
  pviValues: Option[List[Int]]
)

class PropertyValueInnerTable(val propertyType: PropertyTypes) extends SQLSyntaxSupport[PropertyValueInner]:
  override val schemaName = Some("public")
  override val tableName = s"property_value_${propertyType.toString}s"

  def apply(pv: ResultName[PropertyValueInner])(rs: WrappedResultSet) = new PropertyValueInner(
    propertyValueSetPk = UUID.randomUUID(),
    propertyPk = UUID.randomUUID(),
    pvcValues = None,
    pviValues = None
  )

val propertyValueVarchars = new PropertyValueInnerTable(PropertyTypes.varchar)
val propertyValueInts = new PropertyValueInnerTable(PropertyTypes.int)

case class PropertyValues
(
  propertyName: String,
  propertyTypes: List[PropertyTypes],
  propertyValueSetPk: UUID,
  propertyPk: UUID,
  pvcValues: List[String],
  pviValues: List[Int]
)

// TODO: Move apply function to top level and remove this object
object PropertyValues extends SQLSyntaxSupport[PropertyValues]:
  override val schemaName = Some("public")
  override val tableName = "properties"

  def apply(rs: WrappedResultSet) =
    val propertyTypes = rs
      .array("PROPERTY_TYPES")
      .getArray
      .asInstanceOf[Array[Object]]
      .map(s => PropertyTypes.valueOf(s.asInstanceOf[String]))
      .toList
    val pvcValues = rs
      .array("PVC_VALUES")
      .getArray
      .asInstanceOf[Array[Object]]
      .map(_.asInstanceOf[String])
      .toList
    val pviValues = rs
      .array("PVI_VALUES")
      .getArray
      .asInstanceOf[Array[Object]]
      .map(_.asInstanceOf[Int])
      .toList
    new PropertyValues(
      propertyName = rs.string("PROPERTY_NAME"),
      propertyTypes = propertyTypes,
      propertyValueSetPk = UUID.fromString(rs.string("PROPERTY_VALUE_SET_PK")),
      propertyPk = UUID.fromString(rs.string("PROPERTY_PK")),
      pvcValues = pvcValues,
      pviValues = pviValues
    )

val pv1 = PropertyValues.syntax("pv1")

