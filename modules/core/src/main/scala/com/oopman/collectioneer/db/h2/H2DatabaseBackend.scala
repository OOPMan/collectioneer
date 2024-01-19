package com.oopman.collectioneer.db.h2

import distage.*
import com.oopman.collectioneer.db.{h2, traits}

case class H2ProjectedQueryObjects
(
  override val PropertyValueQueries: traits.queries.projected.PropertyValueQueries
) extends traits.queries.ProjectedQueryObjects

case class H2RawQueryObjects
(
  override val CollectionQueries: traits.queries.raw.CollectionQueries,
  override val PropertyCollectionQueries: traits.queries.raw.PropertyCollectionQueries,
  override val PropertyQueries: traits.queries.raw.PropertyQueries,
  override val PropertyValueSetQueries: traits.queries.raw.PropertyValueSetQueries,
  override val PropertyValueVarcharQueries: traits.queries.raw.PropertyValueQueries @Id("Varchar"),
  override val PropertyValueVarbinaryQueries: traits.queries.raw.PropertyValueQueries @Id("Varbinary"),
  override val PropertyValueTinyintQueries: traits.queries.raw.PropertyValueQueries @Id("Tinyint"),
  override val PropertyValueSmallintQueries: traits.queries.raw.PropertyValueQueries @Id("Smallint"),
  override val PropertyValueIntQueries: traits.queries.raw.PropertyValueQueries @Id("Int"),
  override val PropertyValueBigintQueries: traits.queries.raw.PropertyValueQueries @Id("Bigint"),
  override val PropertyValueNumericQueries: traits.queries.raw.PropertyValueQueries @Id("Numeric"),
  override val PropertyValueFloatQueries: traits.queries.raw.PropertyValueQueries @Id("Float"),
  override val PropertyValueDoubleQueries: traits.queries.raw.PropertyValueQueries @Id("Double"),
  override val PropertyValueBooleanQueries: traits.queries.raw.PropertyValueQueries @Id("Boolean"),
  override val PropertyValueDateQueries: traits.queries.raw.PropertyValueQueries @Id("Date"),
  override val PropertyValueTimeQueries: traits.queries.raw.PropertyValueQueries @Id("Time"),
  override val PropertyValueTimestampQueries: traits.queries.raw.PropertyValueQueries @Id("Timestamp"),
  override val PropertyValueCLOBQueries: traits.queries.raw.PropertyValueQueries @Id("CLOB"),
  override val PropertyValueBLOBQueries: traits.queries.raw.PropertyValueQueries @Id("BLOB"),
  override val PropertyValueUUIDQueries: traits.queries.raw.PropertyValueQueries @Id("UUID"),
  override val PropertyValueJSONQueries: traits.queries.raw.PropertyValueQueries @Id("JSON"),
) extends traits.queries.RawQueryObjects

case class H2QueryObjects
(
  override val projected: traits.queries.ProjectedQueryObjects,
  override val raw: traits.queries.RawQueryObjects
) extends traits.queries.QueryObjects

case class H2ProjectedDAOObjects
(
  override val PropertyValueDAO: traits.dao.projected.PropertyValueDAO
) extends traits.dao.ProjectedDAOObjects

case class H2RawDAOObjects
(
  override val CollectionDAO: traits.dao.raw.CollectionDAO,
  override val PropertyCollectionDAO: traits.dao.raw.PropertyCollectionDAO,
  override val PropertyDAO: traits.dao.raw.PropertyDAO,
  override val PropertyValueSetDAO: traits.dao.raw.PropertyValueSetDAO,
) extends traits.dao.RawDAOObjects

case class H2DAOObjects
(
  override val projected: traits.dao.ProjectedDAOObjects,
  override val raw: traits.dao.RawDAOObjects

) extends traits.dao.DAOObjects

case class H2DatabaseBackend
(
  override val queries: traits.queries.QueryObjects,
  override val dao: traits.dao.DAOObjects

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
  make[traits.dao.projected.PropertyValueDAO].from(h2.dao.projected.PropertyValueDAO)
  make[traits.dao.raw.CollectionDAO].from(h2.dao.raw.CollectionDAO)
  make[traits.dao.raw.PropertyDAO].from(h2.dao.raw.PropertyDAO)
  make[traits.dao.raw.PropertyCollectionDAO].from(h2.dao.raw.PropertyCollectionDAO)
  make[traits.dao.raw.PropertyValueSetDAO].from(h2.dao.raw.PropertyValueSetDAO)
  make[traits.queries.QueryObjects].from[H2QueryObjects]
  make[traits.queries.ProjectedQueryObjects].from[H2ProjectedQueryObjects]
  make[traits.queries.RawQueryObjects].from[H2RawQueryObjects]
  make[traits.queries.projected.PropertyValueQueries].from(h2.queries.projected.PropertyValueQueries)
  make[traits.queries.raw.CollectionQueries].from(h2.queries.raw.CollectionQueries)
  make[traits.queries.raw.PropertyCollectionQueries].from(h2.queries.raw.PropertyCollectionQueries)
  make[traits.queries.raw.PropertyQueries].from(h2.queries.raw.PropertyQueries)
  make[traits.queries.raw.PropertyValueSetQueries].from(h2.queries.raw.PropertyValueSetQueries)
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
