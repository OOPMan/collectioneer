package com.oopman.collectioneer.plugins.postgresbacken

import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.db.scalikejdbc.ScalikeJDBCDatabaseBackendPlugin
import com.oopman.collectioneer.plugins.postgresbacken.EmbeddedPostgresDatabaseBackendPlugin.configEmbeddedPostgresMap
import com.oopman.collectioneer.plugins.postgresbackend.PostgresDatabaseBackendModule
import com.oopman.collectioneer.{Config, Plugin}
import com.typesafe.scalalogging.LazyLogging
import de.softwareforge.testing.postgres.embedded.EmbeddedPostgres
import izumi.distage.model.definition.ModuleDef
import izumi.distage.plugins.PluginDef
import org.postgresql.PGProperty
import org.postgresql.ds.PGSimpleDataSource

import java.sql.{Connection, DriverManager}
import java.util.UUID
import javax.sql.DataSource


class PostgresDatabaseBackendPlugin(val config: Config) extends ScalikeJDBCDatabaseBackendPlugin with LazyLogging:
  def getName = "PostgreSQL Database Backend"

  def getShortName = "postgresbackend"

  def getVersion = "master"

  def compatibleWithDatasourceUri: Boolean =
    org.postgresql.Driver().acceptsURL(config.datasourceUri)

  def getDatasource: DataSource =
    val properties = org.postgresql.Driver.parseURL(config.datasourceUri, java.util.Properties())
    val dataSource = PGSimpleDataSource()
    dataSource.setServerNames(Array(PGProperty.PG_HOST.getOrDefault(properties)))
    dataSource.setPortNumbers(Array(PGProperty.PG_PORT.getOrDefault(properties).toInt))
    dataSource.setDatabaseName(PGProperty.PG_DBNAME.getOrDefault(properties))
    dataSource.setUser(PGProperty.USER.getOrDefault(properties))
    dataSource.setPassword(PGProperty.PASSWORD.getOrDefault(properties))
    dataSource

  def getConnection: Connection =
    DriverManager.getConnection(config.datasourceUri)

  def getMigrationLocations: Seq[String] = Seq(
    "classpath:migrations/postgres",
    "classpath:com/oopman/collectioneer/plugins/postgresbackend/migrations"
  )

  override def getDatabaseBackendModule: ModuleDef =
    val scalikeJDBCDatabaseBackendModule = super.getDatabaseBackendModule
    new ModuleDef:
      include(scalikeJDBCDatabaseBackendModule)
      include(PostgresDatabaseBackendModule)

class EmbeddedPostgresDatabaseBackendPlugin(override val config: Config) extends PostgresDatabaseBackendPlugin(config):

  override def compatibleWithDatasourceUri: Boolean =
    config.datasourceUri.startsWith("jdbc:embeddedpostgresql:")

  override def getDatasource: DataSource =
    // TODO: Support non-default datasource
    configEmbeddedPostgresMap.getOrElseUpdate(
      config.datasourceUri,
      EmbeddedPostgres
        .builderWithDefaults()
        // TODO: Customize based on URI
        .setDataDirectory((os.pwd / "collection").wrapped)
        .setRemoveDataOnShutdown(false)
        .setServerVersion("15")
        .build()
    ).createDefaultDataSource()

  override def getConnection: Connection =
    // TODO: Support connectiuon with custom username/password
    getDatasource.getConnection

  override def shutDown(): Unit =
    super.shutDown()
    configEmbeddedPostgresMap.get(config.datasourceUri).foreach(_.close())


object EmbeddedPostgresDatabaseBackendPlugin:
  protected val configEmbeddedPostgresMap: collection.mutable.Map[String, EmbeddedPostgres] = collection.mutable.Map()

class TestPostgresDatabaseBackendPlugin(override val config: Config) extends EmbeddedPostgresDatabaseBackendPlugin(config):
  protected lazy val embeddedPostgres = EmbeddedPostgres
    .builderWithDefaults()
    .setDataDirectory((os.pwd / UUID.randomUUID().toString).wrapped)
    .setRemoveDataOnShutdown(true)
    .setServerVersion("15")
    .build()

  override def compatibleWithDatasourceUri: Boolean =
    config.datasourceUri == "jdbc:testpostgresql"

  override def getDatasource: DataSource =
    embeddedPostgres.createDefaultDataSource()

  override def shutDown(): Unit = embeddedPostgres.close()


object PostgresDatabaseBackendPluginDef extends PluginDef:
  many[DatabaseBackendPlugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
    .add[TestPostgresDatabaseBackendPlugin]
  many[ScalikeJDBCDatabaseBackendPlugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
    .add[TestPostgresDatabaseBackendPlugin]
  many[Plugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
    .add[TestPostgresDatabaseBackendPlugin]
