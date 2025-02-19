package com.oopman.collectioneer.db.scalikejdbc.entity

import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import scalikejdbc.*

import scala.reflect.ClassTag

object Utils {
  def resultSetArrayToListOf[T: ClassTag](rs: WrappedResultSet, columnLabel: String): List[T] =
    rs.array(columnLabel)
      .getArray
      .asInstanceOf[Array[T]]
      .toList

  def resultSetArrayToPropertyTypeList(rs: WrappedResultSet, columnLabel: String): List[PropertyType] =
    resultSetArrayToListOf[String](rs, columnLabel).map(PropertyType.valueOf)
}
