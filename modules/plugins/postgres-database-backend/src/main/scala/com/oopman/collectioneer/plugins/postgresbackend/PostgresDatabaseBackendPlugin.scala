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

import java.sql.Connection
import javax.sql.DataSource


class PostgresDatabaseBackendPlugin(val config: Config) extends ScalikeJDBCDatabaseBackendPlugin with LazyLogging:
  def getName = "PostgreSQL Database Backend"

  def getShortName = "postgresbackend"

  def getVersion = "master"

  def compatibleWithDatasourceUri: Boolean =
    org.postgresql.Driver().acceptsURL(config.datasourceUri)

  def getDatasource: DataSource = ???

  def getConnection: Connection = ???

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
    org.postgresql.Driver().acceptsURL(config.datasourceUri.replace("jdbc:embeddedpostgresql:", "jdbc:postgresql:"))

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


object PostgresDatabaseBackendPluginDef extends PluginDef:
  many[DatabaseBackendPlugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
  many[ScalikeJDBCDatabaseBackendPlugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
  many[Plugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
