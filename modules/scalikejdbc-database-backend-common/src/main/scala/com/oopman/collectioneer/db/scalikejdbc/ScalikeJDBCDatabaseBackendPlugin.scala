package com.oopman.collectioneer.db.scalikejdbc

import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.db.scalikejdbc.dao.DAOImplModule
import izumi.distage.model.definition.ModuleDef
import org.flywaydb.core.Flyway
import scalikejdbc.*

import java.sql.Connection
import javax.sql.DataSource

type DBConnectionProvider = () => DBConnection

trait ScalikeJDBCDatabaseBackendPlugin extends DatabaseBackendPlugin:
  override def startUp(): Unit =
    val dataSource = getDatasource
    if !ConnectionPool.isInitialized(config.datasourceUri)
    then
      val dataSourceConnectionPool = DataSourceConnectionPool(dataSource)
      ConnectionPool.add(config.datasourceUri, dataSourceConnectionPool)
      ConnectionPool.singleton(dataSourceConnectionPool)
    Flyway
      .configure()
      .dataSource(dataSource)
      .locations(getMigrationLocations*)
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
      include(DAOImplModule)
