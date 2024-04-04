package com.oopman.collectioneer.db.scalikejdbc

import com.oopman.collectioneer.db.DatabaseBackendPlugin
import izumi.distage.model.definition.ModuleDef
import org.flywaydb.core.Flyway
import scalikejdbc.*

import java.sql.Connection
import javax.sql.DataSource

trait ScalikeJDBCDatabaseBackendPlugin extends DatabaseBackendPlugin:
  override def startUp(): Unit =
    val dataSource = getDatasource
    if !ConnectionPool.isInitialized(config.datasourceUri)
    then ConnectionPool.add(config.datasourceUri, DataSourceConnectionPool(dataSource))
    Flyway
      .configure()
      .dataSource(dataSource)
      .locations(getMigrationLocations: _*)
      .load()
      .migrate()
  override def shutDown(): Unit =
    ConnectionPool.close(config.datasourceUri)
  def getDatasource: DataSource
  def getConnection: Connection
  def getMigrationLocations: Seq[String]

  override def getDatabaseBackendModule: ModuleDef =
    new ModuleDef:
      make[DBConnectionProvider].from(() => NamedDB(config.datasourceUri))
