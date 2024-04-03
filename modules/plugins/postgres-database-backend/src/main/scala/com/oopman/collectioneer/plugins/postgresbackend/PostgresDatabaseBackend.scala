package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.db.scalikejdbc.traits
import com.oopman.collectioneer.plugins.postgresbackend
import distage.*

case class PostgresProjectedDAOs
(
  CollectionDAO: traits.dao.projected.CollectionDAO,
  PropertyDAO: traits.dao.projected.PropertyDAO,
  PropertyValueDAO: traits.dao.projected.PropertyValueDAO
) extends traits.dao.ProjectedDAOs

case class PostgresRawDAOs
(
  CollectionDAO: traits.dao.raw.CollectionDAO,
  PropertyCollectionDAO: traits.dao.raw.PropertyCollectionDAO,
  PropertyDAO: traits.dao.raw.PropertyDAO,
  RelationshipCollectionDAO: traits.dao.raw.RelationshipCollectionDAO,
  RelationshipDAO: traits.dao.raw.RelationshipDAO
) extends traits.dao.RawDAOs

case class PostgresDAOs
(
  projected: traits.dao.ProjectedDAOs,
  raw: traits.dao.RawDAOs

) extends traits.dao.DAOs

case class PostgresDatabaseBackend
(
  dao: traits.dao.DAOs

) extends traits.dao.DatabaseBackend:
  val migrationLocations: Seq[String] = Seq(
    "classpath:migrations/postgres",
    "classpath:com/oopman/collectioneer/db/postgres/migrations"
  )

val PostgresDatabaseBackendModule = new ModuleDef:
  make[traits.dao.DatabaseBackend].from[PostgresDatabaseBackend]
  make[traits.dao.DAOs].from[PostgresDAOs]
  make[traits.dao.ProjectedDAOs].from[PostgresProjectedDAOs]
  make[traits.dao.RawDAOs].from[PostgresRawDAOs]
  make[traits.dao.projected.CollectionDAO].from(postgresbackend.dao.projected.CollectionDAO)
  make[traits.dao.projected.PropertyDAO].from(postgresbackend.dao.projected.PropertyDAO)
  make[traits.dao.projected.PropertyValueDAO].from(postgresbackend.dao.projected.PropertyValueDAO)
  make[traits.dao.raw.CollectionDAO].from(postgresbackend.dao.raw.CollectionDAO)
  make[traits.dao.raw.PropertyCollectionDAO].from(postgresbackend.dao.raw.PropertyCollectionDAO)
  make[traits.dao.raw.PropertyDAO].from(postgresbackend.dao.raw.PropertyDAO)
  make[traits.dao.raw.RelationshipCollectionDAO].from(postgresbackend.dao.raw.RelationshipCollectionDAO)
  make[traits.dao.raw.RelationshipDAO].from(postgresbackend.dao.raw.RelationshipDAO)

