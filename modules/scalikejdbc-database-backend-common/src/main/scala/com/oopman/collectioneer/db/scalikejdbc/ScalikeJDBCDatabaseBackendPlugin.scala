package com.oopman.collectioneer.db.scalikejdbc

import com.oopman.collectioneer.db.DatabaseBackendPlugin
import izumi.distage.model.definition.ModuleDef
import scalikejdbc.*

import java.sql.Connection
import javax.sql.DataSource

trait ScalikeJDBCDatabaseBackendPlugin extends DatabaseBackendPlugin:
  override def startUp(): Unit =
    if !ConnectionPool.isInitialized(config.datasourceUri)
    then ConnectionPool.add(config.datasourceUri, DataSourceConnectionPool(getDatasource))
    // TODO: Run Flyway migrations?
  override def shutDown(): Unit =
    ConnectionPool.close(config.datasourceUri)
  def getDatasource: DataSource
  def getConnection: Connection
  def getMigrationLocations: Seq[String]

  override def getDatabaseBackendModule: ModuleDef =
    new ModuleDef:
      make[DBConnectionProvider].from(() => NamedDB(config.datasourceUri))
