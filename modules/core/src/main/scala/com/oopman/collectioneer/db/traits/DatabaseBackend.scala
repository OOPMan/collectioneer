package com.oopman.collectioneer.db.traits

import com.oopman.collectioneer.db.traits

//import com.oopman.collectioneer.db.{dao -> daoPackage}
//
trait DatabaseBackend:
  val migrationLocations: Seq[String]
  val queries: traits.queries.QueryObjects
  val dao: traits.dao.DAOObjects
//
//object H2DatabaseBackend extends DatabaseBackend:
//  override val migrationLocations: Seq[String] = Seq(
//    "classpath:migrations/h2",
//    "classpath:com/oopman/collectioneer/db/migrations/h2"
//  )
//  val queries: QueryObjects = new QueryObjects:
//    override val projected: ProjectedQueryObjects = new ProjectedQueryObjects:
//      override val PropertyValueQueries: PropertyValueQueries = projected.PropertyValueQueries
//    override val raw: RawQueryObjects = new RawQueryObjects:
//      override val CollectionQueries: CollectionQueries = ???
//      override val PropertyCollectionQueries: PropertyCollectionQueries = ???
//      override val PropertyQueries: PropertyQueries = ???
//      override val PropertyValueQueries: PropertyValueQueries = ???
//      override val PropertyValueSetQueries: PropertyValueSetQueries = ???
//  val dao: DAOObjects = ???
