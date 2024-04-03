package com.oopman.collectioneer.db.scalikejdbc.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw
import scalikejdbc.{QuerySQLSyntaxProvider, ResultName, SQLSyntaxSupport, WrappedResultSet}

abstract class PropertyValueSQLSyntaxSupport[T <: raw.PropertyValue[?]]
(override val tableName: String, override val schemaName: Option[String] = Some("public")) extends SQLSyntaxSupport[T]:

  def apply(pv: ResultName[T])(rs: WrappedResultSet): T

  val syntax1: QuerySQLSyntaxProvider[SQLSyntaxSupport[T], T] = this.syntax(f"${tableName}_1")
