package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.db.traits
import distage.*

case class PostgresProjectedQueryObjects
(
  PropertyValueQueries: traits.queries.projected.PropertyValueQueries
) extends traits.queries.ProjectedQueryObjects

case class PostgresRawQueryObjects
(
  CollectionQueries: traits.queries.raw.CollectionQueries,
  PropertyCollectionQueries: traits.queries.raw.PropertyCollectionQueries,
  PropertyQueries: traits.queries.raw.PropertyQueries,
  PropertyValueVarcharQueries: traits.queries.raw.PropertyValueQueries @Id("Varchar"),
  PropertyValueVarbinaryQueries: traits.queries.raw.PropertyValueQueries @Id("Varbinary"),
  PropertyValueTinyintQueries: traits.queries.raw.PropertyValueQueries @Id("Tinyint"),
  PropertyValueSmallintQueries: traits.queries.raw.PropertyValueQueries @Id("Smallint"),
  PropertyValueIntQueries: traits.queries.raw.PropertyValueQueries @Id("Int"),
  PropertyValueBigintQueries: traits.queries.raw.PropertyValueQueries @Id("Bigint"),
  PropertyValueNumericQueries: traits.queries.raw.PropertyValueQueries @Id("Numeric"),
  PropertyValueFloatQueries: traits.queries.raw.PropertyValueQueries @Id("Float"),
  PropertyValueDoubleQueries: traits.queries.raw.PropertyValueQueries @Id("Double"),
  PropertyValueBooleanQueries: traits.queries.raw.PropertyValueQueries @Id("Boolean"),
  PropertyValueDateQueries: traits.queries.raw.PropertyValueQueries @Id("Date"),
  PropertyValueTimeQueries: traits.queries.raw.PropertyValueQueries @Id("Time"),
  PropertyValueTimestampQueries: traits.queries.raw.PropertyValueQueries @Id("Timestamp"),
  PropertyValueCLOBQueries: traits.queries.raw.PropertyValueQueries @Id("CLOB"),
  PropertyValueBLOBQueries: traits.queries.raw.PropertyValueQueries @Id("BLOB"),
  PropertyValueUUIDQueries: traits.queries.raw.PropertyValueQueries @Id("UUID"),
  PropertyValueJSONQueries: traits.queries.raw.PropertyValueQueries @Id("JSON"),
  RelationshipCollectionQueries: traits.queries.raw.RelationshipCollectionQueries,
  RelationshipQueries: traits.queries.raw.RelationshipQueries
) extends traits.queries.RawQueryObjects

case class PostgresQueryObjects
(
  projected: traits.queries.ProjectedQueryObjects,
  raw: traits.queries.RawQueryObjects
) extends traits.queries.QueryObjects

case class PostgresProjectedDAOObjects
(
  CollectionDAO: traits.dao.projected.CollectionDAO,
  PropertyDAO: traits.dao.projected.PropertyDAO,
  PropertyValueDAO: traits.dao.projected.PropertyValueDAO
) extends traits.dao.ProjectedDAOObjects

case class PostgresRawDAOObjects
(
  CollectionDAO: traits.dao.raw.CollectionDAO,
  PropertyCollectionDAO: traits.dao.raw.PropertyCollectionDAO,
  PropertyDAO: traits.dao.raw.PropertyDAO,
  RelationshipCollectionDAO: traits.dao.raw.RelationshipCollectionDAO,
  RelationshipDAO: traits.dao.raw.RelationshipDAO
) extends traits.dao.RawDAOObjects

case class PostgresDAOObjects
(
  projected: traits.dao.ProjectedDAOObjects,
  raw: traits.dao.RawDAOObjects

) extends traits.dao.DAOObjects

case class PostgresDatabaseBackend
(
  queries: traits.queries.QueryObjects,
  dao: traits.dao.DAOObjects

) extends traits.DatabaseBackend:
  override val migrationLocations: Seq[String] = Seq(
    "classpath:migrations/postgres",
    "classpath:com/oopman/collectioneer/db/postgres/migrations"
  )

val PostgresDatabaseBackendModule = new ModuleDef:
  make[traits.DatabaseBackend].from[PostgresDatabaseBackend]
  make[traits.dao.DAOObjects].from[PostgresDAOObjects]
  make[traits.dao.ProjectedDAOObjects].from[PostgresProjectedDAOObjects]
  make[traits.dao.RawDAOObjects].from[PostgresRawDAOObjects]
  make[traits.dao.projected.CollectionDAO].from(dao.projected.CollectionDAO)
  make[traits.dao.projected.PropertyDAO].from(dao.projected.PropertyDAO)
  make[traits.dao.projected.PropertyValueDAO].from(dao.projected.PropertyValueDAO)
  make[traits.dao.raw.CollectionDAO].from(dao.raw.CollectionDAO)
  make[traits.dao.raw.PropertyCollectionDAO].from(dao.raw.PropertyCollectionDAO)
  make[traits.dao.raw.PropertyDAO].from(dao.raw.PropertyDAO)
  make[traits.dao.raw.RelationshipCollectionDAO].from(dao.raw.RelationshipCollectionDAO)
  make[traits.dao.raw.RelationshipDAO].from(dao.raw.RelationshipDAO)
  make[traits.queries.QueryObjects].from[PostgresQueryObjects]
  make[traits.queries.ProjectedQueryObjects].from[PostgresProjectedQueryObjects]
  make[traits.queries.RawQueryObjects].from[PostgresRawQueryObjects]
  make[traits.queries.projected.PropertyValueQueries].from(queries.projected.PropertyValueQueries)
  make[traits.queries.raw.CollectionQueries].from(queries.raw.CollectionQueries)
  make[traits.queries.raw.PropertyCollectionQueries].from(queries.raw.PropertyCollectionQueries)
  make[traits.queries.raw.PropertyQueries].from(queries.raw.PropertyQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Varchar").from(queries.raw.PropertyValueVarcharQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Varbinary").from(queries.raw.PropertyValueVarbinaryQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Smallint").from(queries.raw.PropertyValueSmallintQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Int").from(queries.raw.PropertyValueIntQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Bigint").from(queries.raw.PropertyValueBigintQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Numeric").from(queries.raw.PropertyValueNumericQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Float").from(queries.raw.PropertyValueFloatQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Double").from(queries.raw.PropertyValueDoubleQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Boolean").from(queries.raw.PropertyValueBooleanQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Date").from(queries.raw.PropertyValueDateQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Time").from(queries.raw.PropertyValueTimeQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Timestamp").from(queries.raw.PropertyValueTimestampQueries)
  make[traits.queries.raw.PropertyValueQueries].named("UUID").from(queries.raw.PropertyValueUUIDQueries)
  make[traits.queries.raw.PropertyValueQueries].named("JSON").from(queries.raw.PropertyValueJSONQueries)
  make[traits.queries.raw.RelationshipCollectionQueries].from(queries.raw.RelationshipCollectionQueries)
  make[traits.queries.raw.RelationshipQueries].from(queries.raw.RelationshipQueries)
