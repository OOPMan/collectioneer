package com.oopman.collectioneer.db.scalikejdbc.entity

import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import scalikejdbc.*

import scala.reflect.ClassTag

object Utils {
  def resultSetArray(rs: WrappedResultSet, columnLabel: String): Array[Object] = rs
    .array(columnLabel)
    .getArray
    .asInstanceOf[Array[Object]]

  def resultSetArrayToList[T: ClassTag](rs: WrappedResultSet, columnLabel: String)(transformer: Object => T): List[T] =
    resultSetArray(rs, columnLabel)
      .map(transformer)
      .toList

  def resultSetArrayToListOf[T: ClassTag](rs: WrappedResultSet, columnLabel: String): List[T] =
    resultSetArrayToList[T](rs, columnLabel)(_.asInstanceOf[T])

  def resultSetArrayToPropertyTypeList(rs: WrappedResultSet, columnLabel: String): List[PropertyType] =
    resultSetArrayToListOf[String](rs, columnLabel).map(PropertyType.valueOf)
}
