package com.oopman.collectioneer.db.entity

import scalikejdbc._

import scala.reflect.ClassTag

object Utils {
  def resultSetArray(rs: WrappedResultSet, columnLabel: String): Array[Object] = rs
    .array(columnLabel)
    .getArray
    .asInstanceOf[Array[Object]]

  def resultSetArrayToList[T:ClassTag](rs: WrappedResultSet, columnLabel: String): List[T] =
    resultSetArray(rs, columnLabel)
      .map(_.asInstanceOf[T])
      .toList
}
