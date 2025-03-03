package com.oopman.collectioneer.plugins.postgresbacken

import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.db.scalikejdbc.ScalikeJDBCDatabaseBackendPlugin
//import com.oopman.collectioneer.plugins.postgresbacken.EmbeddedPostgresDatabaseBackendPlugin.configEmbeddedPostgresMap
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
    config.datasourceUri.exists(org.postgresql.Driver().acceptsURL(_))

  def getDatasource: DataSource = config.datasourceUri match {
    case Some(datasourceUri) =>
      val properties = org.postgresql.Driver.parseURL(datasourceUri, java.util.Properties())
      val dataSource = PGSimpleDataSource()
      dataSource.setServerNames(Array(PGProperty.PG_HOST.getOrDefault(properties)))
      dataSource.setPortNumbers(Array(PGProperty.PG_PORT.getOrDefault(properties).toInt))
      dataSource.setDatabaseName(PGProperty.PG_DBNAME.getOrDefault(properties))
      dataSource.setUser(PGProperty.USER.getOrDefault(properties))
      dataSource.setPassword(PGProperty.PASSWORD.getOrDefault(properties))
      dataSource
    case _ => throw RuntimeException("config.datasourceUri is undefined!")  
  }

  def getConnection: Connection = config.datasourceUri match {
    case Some(datasourceUri) => DriverManager.getConnection(datasourceUri)
    case _ => throw RuntimeException("config.datasourceUri is undefined")
  }

  def getMigrationLocations: Seq[String] = Seq(
    "classpath:migrations/postgres",
    "classpath:com/oopman/collectioneer/plugins/postgresbackend/migrations"
  )

  override def getDatabaseBackendModule: ModuleDef =
    val scalikeJDBCDatabaseBackendModule = super.getDatabaseBackendModule
    new ModuleDef:
      include(scalikeJDBCDatabaseBackendModule)
      include(PostgresDatabaseBackendModule)


//    configEmbeddedPostgresMap.get(config.datasourceUri).foreach(_.close())

