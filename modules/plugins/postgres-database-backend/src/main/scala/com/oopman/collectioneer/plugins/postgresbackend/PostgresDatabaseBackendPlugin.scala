package com.oopman.collectioneer.plugins.postgresbacken

import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.db.scalikejdbc.ScalikeJDBCDatabaseBackendPlugin
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

  def getDatabaseBackendModule: ModuleDef = PostgresDatabaseBackendModule

  def startUp(): Boolean = ???

  def shutDown(): Boolean = ???


class EmbeddedPostgresDatabaseBackendPlugin(override val config: Config) extends PostgresDatabaseBackendPlugin(config):

  // TODO: Move this to a mutable map of config.datasourceUri -> EmbeddedPostgres to avoid it getting removed by the Injector
  private lazy val embeddedPostgres = EmbeddedPostgres
    .builderWithDefaults()
    // TODO: Customize based on constructor parameter
    .build()

  override def compatibleWithDatasourceUri: Boolean =
    org.postgresql.Driver().acceptsURL(config.datasourceUri.replace("jdbc:embeddedpostgresql:", "jdbc:postgresql:"))

  override def getDatasource: DataSource =
    // TODO: Support non-default datasource
    embeddedPostgres.createDefaultDataSource()

  override def getConnection: Connection =
    // TODO: Support connectiuon with custom username/password
    getDatasource.getConnection

  override def startUp(): Boolean = super.startUp()

  override def shutDown(): Boolean = super.shutDown()


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
