package com.oopman.collectioneer.db.traits.queries

trait ProjectedQueryObjects:
  val PropertyValueQueries: projected.PropertyValueQueries

trait RawQueryObjects:
  val CollectionQueries: raw.CollectionQueries
//  val PropertyCollectionQueries: raw.PropertyCollectionQueries
//  val PropertyQueries: raw.PropertyQueries
//  val PropertyValueVarcharQueries: raw.PropertyValueQueries
//  val PropertyValueSetQueries: raw.PropertyValueSetQueries
//
trait QueryObjects:
  val projected: ProjectedQueryObjects
  val raw: RawQueryObjects

