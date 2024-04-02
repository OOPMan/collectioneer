package com.oopman.collectioneer.db

import com.oopman.collectioneer.Config
import distage.*
import distage.plugins.PluginConfig
import izumi.distage.plugins.load.PluginLoader
import izumi.fundamentals.platform.functional.Identity
import scalikejdbc.*

import java.sql.Connection
import javax.sql.DataSource

type DBConnectionProvider = () => DBConnection

object Injection:
  def getInjectorAndModule[F[_], A](config: Config): (Injector[Identity], ModuleDef) =
    val injector = Injector()
    val pluginConfig: PluginConfig = PluginConfig.cached("com.oopman.collectioneer.plugins")
    val pluginModules: ModuleBase = PluginLoader().load(pluginConfig).result.merge
    val baseModule = new ModuleDef:
      include(pluginModules)
      make[Config].from(config)
    val databaseBackendPlugin: DatabaseBackendPlugin = injector.produceRun(baseModule) {
      (databaseBackendPlugins: Set[DatabaseBackendPlugin]) => databaseBackendPlugins.filter(_.compatibleWithDatasourceUri).head
    }
    val finalModule = new ModuleDef:
      include(baseModule)
      include(dao.DAOModule)
      include(databaseBackendPlugin.getDatabaseBackendModule)
      make[DataSource].from(databaseBackendPlugin.getDatasource)
      make[Connection].from(databaseBackendPlugin.getConnection)
      make[DBConnectionProvider].from(() => {
        if !ConnectionPool.isInitialized(config.datasourceUri) then ConnectionPool.add(config.datasourceUri, DataSourceConnectionPool(databaseBackendPlugin.getDatasource))
        NamedDB(config.datasourceUri)
      })
    (injector, finalModule)

  def produceRun[F[_], A](config: Config): Functoid[Identity[A]] => Identity[A] =
    val (injector, module) = getInjectorAndModule(config)
    injector.produceRun(module)
