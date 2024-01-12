package com.oopman.collectioneer.db.traits.queries

trait ProjectedQueryObjects:
  val PropertyValueQueries: projected.PropertyValueQueries

trait RawQueryObjects:
  val CollectionQueries: raw.CollectionQueries
  val PropertyCollectionQueries: raw.PropertyCollectionQueries
  val PropertyQueries: raw.PropertyQueries
  val PropertyValueVarcharQueries: raw.PropertyValueQueries
  val PropertyValueVarbinaryQueries: raw.PropertyValueQueries
  val PropertyValueTinyintQueries: raw.PropertyValueQueries
  val PropertyValueSmallintQueries: raw.PropertyValueQueries
  val PropertyValueIntQueries: raw.PropertyValueQueries
  val PropertyValueBigintQueries: raw.PropertyValueQueries
  val PropertyValueNumericQueries: raw.PropertyValueQueries
  val PropertyValueFloatQueries: raw.PropertyValueQueries
  val PropertyValueDoubleQueries: raw.PropertyValueQueries
  val PropertyValueBooleanQueries: raw.PropertyValueQueries
  val PropertyValueDateQueries: raw.PropertyValueQueries
  val PropertyValueTimeQueries: raw.PropertyValueQueries
  val PropertyValueTimestampQueries: raw.PropertyValueQueries
  val PropertyValueCLOBQueries: raw.PropertyValueQueries
  val PropertyValueBLOBQueries: raw.PropertyValueQueries
  val PropertyValueUUIDQueries: raw.PropertyValueQueries
  val PropertyValueJSONQueries: raw.PropertyValueQueries
  val PropertyValueSetQueries: raw.PropertyValueSetQueries

trait QueryObjects:
  val projected: ProjectedQueryObjects
  val raw: RawQueryObjects

