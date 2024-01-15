package com.oopman.collectioneer.db.h2.entity.raw.propertyvalue

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.raw.PropertyValue
import scalikejdbc.*

abstract class PropertyValueSQLSyntaxSupport[T <: PropertyValue[?]](override val tableName: String) extends SQLSyntaxSupport[T]:
  override val schemaName = Some("public")

  def apply(pv: ResultName[T])(rs: WrappedResultSet): T

  val syntax1: QuerySQLSyntaxProvider[SQLSyntaxSupport[T], T] = this.syntax(f"${tableName}_1")
