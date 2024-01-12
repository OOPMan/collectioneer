package com.oopman.collectioneer.db.h2

import com.oopman.collectioneer.db.traits.dao.projected.PropertyValueDAO
import com.oopman.collectioneer.db.traits.dao.raw.CollectionDAO
import com.oopman.collectioneer.db.traits.dao.{DAOObjects, ProjectedDAOObjects, RawDAOObjects}
import com.oopman.collectioneer.db.traits.queries.projected.PropertyValueQueries
import com.oopman.collectioneer.db.traits.queries.raw.CollectionQueries
import com.oopman.collectioneer.db.traits.queries.{ProjectedQueryObjects, QueryObjects, RawQueryObjects}
import distage.*
import com.oopman.collectioneer.db.{h2, traits}

//object H2DatabaseBackend extends traits.DatabaseBackend:
//  override val migrationLocations: Seq[String] = Seq(
//    "classpath:migrations/h2",
//    "classpath:com/oopman/collectioneer/db/migrations/h2"
//  )
//  override val queries: traits.queries.QueryObjects = new traits.queries.QueryObjects:
//    override val projected = new traits.queries.ProjectedQueryObjects:
//      override val PropertyValueQueries = h2.queries.projected.PropertyValueQueries
//    override val raw = new traits.queries.RawQueryObjects:
//      override val CollectionQueries = h2.queries.raw.CollectionQueries
//      override val PropertyCollectionQueries = h2.queries.raw.PropertyCollectionQueries
//      override val PropertyQueries = h2.queries.raw.PropertyQueries
//      override val PropertyValueVarcharQueries = h2.queries.raw.PropertyValueVarcharQueries
//      override val PropertyValueSetQueries = h2.queries.raw.PropertyValueSetQueries
//  override val dao = ???

case class H2ProjectedQueryObjects
(
  override val PropertyValueQueries: PropertyValueQueries
) extends traits.queries.ProjectedQueryObjects

case class H2RawQueryObjects
(
  override val CollectionQueries: CollectionQueries
) extends traits.queries.RawQueryObjects

case class H2QueryObjects
(
  override val projected: ProjectedQueryObjects,
  override val raw: RawQueryObjects
) extends traits.queries.QueryObjects

case class H2ProjectedDAOObjects
(
  override val PropertyValueDAO: PropertyValueDAO
) extends traits.dao.ProjectedDAOObjects

case class H2RawDAOObjects
(
  override val CollectionDAO: CollectionDAO
) extends traits.dao.RawDAOObjects

case class H2DAOObjects
(
  override val projected: ProjectedDAOObjects,
  override val raw: RawDAOObjects

) extends traits.dao.DAOObjects

case class H2DatabaseBackend
(
  override val queries: QueryObjects,
  override val dao: DAOObjects

) extends traits.DatabaseBackend:
  override val migrationLocations: Seq[String] = Seq(
    "classpath:migrations/h2",
    "classpath:com/oopman/collectioneer/db/migrations/h2"
  )

val H2DatabaseBackendModule = new ModuleDef:
  make[traits.DatabaseBackend].from[H2DatabaseBackend]
  make[traits.dao.DAOObjects].from[H2DAOObjects]
  make[traits.dao.ProjectedDAOObjects].from[H2ProjectedDAOObjects]
  make[traits.dao.RawDAOObjects].from[H2RawDAOObjects]
  make[traits.dao.projected.PropertyValueDAO].from(h2.dao.projected.PropertyValueDAO)
  make[traits.dao.raw.CollectionDAO].from(h2.dao.raw.CollectionDAO)
  make[traits.queries.QueryObjects].from[H2QueryObjects]
  make[traits.queries.ProjectedQueryObjects].from[H2ProjectedQueryObjects]
  make[traits.queries.RawQueryObjects].from[H2RawQueryObjects]
  make[traits.queries.projected.PropertyValueQueries].from(h2.queries.projected.PropertyValueQueries)
  make[traits.queries.raw.CollectionQueries].from(h2.queries.raw.CollectionQueries)
