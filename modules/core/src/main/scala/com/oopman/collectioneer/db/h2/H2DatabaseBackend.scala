package com.oopman.collectioneer.db.h2

import com.oopman.collectioneer.db.{h2, traits}
import distage.*

case class H2ProjectedQueryObjects
(
  PropertyValueQueries: traits.queries.projected.PropertyValueQueries
) extends traits.queries.ProjectedQueryObjects

case class H2RawQueryObjects
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

case class H2QueryObjects
(
  projected: traits.queries.ProjectedQueryObjects,
  raw: traits.queries.RawQueryObjects
) extends traits.queries.QueryObjects

case class H2ProjectedDAOObjects
(
  CollectionDAO: traits.dao.projected.CollectionDAO,
  PropertyDAO: traits.dao.projected.PropertyDAO,
  PropertyValueDAO: traits.dao.projected.PropertyValueDAO
) extends traits.dao.ProjectedDAOObjects

case class H2RawDAOObjects
(
  CollectionDAO: traits.dao.raw.CollectionDAO,
  PropertyCollectionDAO: traits.dao.raw.PropertyCollectionDAO,
  PropertyDAO: traits.dao.raw.PropertyDAO,
  RelationshipCollectionDAO: traits.dao.raw.RelationshipCollectionDAO,
  RelationshipDAO: traits.dao.raw.RelationshipDAO
) extends traits.dao.RawDAOObjects

case class H2DAOObjects
(
  projected: traits.dao.ProjectedDAOObjects,
  raw: traits.dao.RawDAOObjects

) extends traits.dao.DAOObjects

case class H2DatabaseBackend
(
  queries: traits.queries.QueryObjects,
  dao: traits.dao.DAOObjects

) extends traits.DatabaseBackend:
  override val migrationLocations: Seq[String] = Seq(
    "classpath:migrations/h2",
    "classpath:com/oopman/collectioneer/db/h2/migrations"
  )

val H2DatabaseBackendModule = new ModuleDef:
  make[traits.DatabaseBackend].from[H2DatabaseBackend]
  make[traits.dao.DAOObjects].from[H2DAOObjects]
  make[traits.dao.ProjectedDAOObjects].from[H2ProjectedDAOObjects]
  make[traits.dao.RawDAOObjects].from[H2RawDAOObjects]
  make[traits.dao.projected.CollectionDAO].from(h2.dao.projected.CollectionDAO)
  make[traits.dao.projected.PropertyDAO].from(h2.dao.projected.PropertyDAO)
  make[traits.dao.projected.PropertyValueDAO].from(h2.dao.projected.PropertyValueDAO)
  make[traits.dao.raw.CollectionDAO].from(h2.dao.raw.CollectionDAO)
  make[traits.dao.raw.PropertyCollectionDAO].from(h2.dao.raw.PropertyCollectionDAO)
  make[traits.dao.raw.PropertyDAO].from(h2.dao.raw.PropertyDAO)
  make[traits.dao.raw.RelationshipCollectionDAO].from(h2.dao.raw.RelationshipCollectionDAO)
  make[traits.dao.raw.RelationshipDAO].from(h2.dao.raw.RelationshipDAO)
  make[traits.queries.QueryObjects].from[H2QueryObjects]
  make[traits.queries.ProjectedQueryObjects].from[H2ProjectedQueryObjects]
  make[traits.queries.RawQueryObjects].from[H2RawQueryObjects]
  make[traits.queries.projected.PropertyValueQueries].from(h2.queries.projected.PropertyValueQueries)
  make[traits.queries.raw.CollectionQueries].from(h2.queries.raw.CollectionQueries)
  make[traits.queries.raw.PropertyQueries].from(h2.queries.raw.PropertyQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Varchar").from(h2.queries.raw.PropertyValueVarcharQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Varbinary").from(h2.queries.raw.PropertyValueVarbinaryQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Tinyint").from(h2.queries.raw.PropertyValueTinyintQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Smallint").from(h2.queries.raw.PropertyValueSmallintQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Int").from(h2.queries.raw.PropertyValueIntQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Bigint").from(h2.queries.raw.PropertyValueBigintQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Numeric").from(h2.queries.raw.PropertyValueNumericQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Float").from(h2.queries.raw.PropertyValueFloatQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Double").from(h2.queries.raw.PropertyValueDoubleQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Boolean").from(h2.queries.raw.PropertyValueBooleanQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Date").from(h2.queries.raw.PropertyValueDateQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Time").from(h2.queries.raw.PropertyValueTimeQueries)
  make[traits.queries.raw.PropertyValueQueries].named("Timestamp").from(h2.queries.raw.PropertyValueTimestampQueries)
  make[traits.queries.raw.PropertyValueQueries].named("CLOB").from(h2.queries.raw.PropertyValueCLOBQueries)
  make[traits.queries.raw.PropertyValueQueries].named("BLOB").from(h2.queries.raw.PropertyValueBLOBQueries)
  make[traits.queries.raw.PropertyValueQueries].named("UUID").from(h2.queries.raw.PropertyValueUUIDQueries)
  make[traits.queries.raw.PropertyValueQueries].named("JSON").from(h2.queries.raw.PropertyValueJSONQueries)
