package com.oopman.collectioneer.db.entity.projected

import com.oopman.collectioneer.db.entity.PropertyType
import scalikejdbc.*

import java.util.UUID

case class PropertyValues
(
  propertyName: String,
  propertyTypes: List[PropertyType],
  propertyValueSetPk: UUID,
  propertyPk: UUID,
  pvcValues: List[String],
  pviValues: List[Int]
)

def generatePropertyValuesFromWrappedResultSet(rs: WrappedResultSet) =
  val propertyTypes = rs
    .array("PROPERTY_TYPES")
    .getArray
    .asInstanceOf[Array[Object]]
    .map(s => PropertyType.valueOf(s.asInstanceOf[String]))
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
  PropertyValues(
    propertyName = rs.string("PROPERTY_NAME"),
    propertyTypes = propertyTypes,
    propertyValueSetPk = UUID.fromString(rs.string("PROPERTY_VALUE_SET_PK")),
    propertyPk = UUID.fromString(rs.string("PROPERTY_PK")),
    pvcValues = pvcValues,
    pviValues = pviValues
  )
