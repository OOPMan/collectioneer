package com.oopman.collectioneer.db

import scalikejdbc.*
import distage.*
import distage.plugins.PluginConfig
import izumi.distage.plugins.load.PluginLoader
import izumi.fundamentals.platform.functional.Identity

import java.sql.Connection
import javax.sql.DataSource

type DBConnectionProvider = () => DBConnection

object Injection:
  def getInjectorAndModule[F[_A], A](dbProvider: DBConnectionProvider): (Injector[Identity], ModuleDef) =
    val injector = Injector()
    val pluginConfig: PluginConfig = PluginConfig.cached("com.oopman.collectioneer.plugins")
    val pluginModules: ModuleBase = PluginLoader().load(pluginConfig).result.merge
    val module = new ModuleDef:
      include(dao.DAOModule)
      include(pluginModules)
      // TODO: Database backend should be selected based on the actual database connection type
      include(h2.H2DatabaseBackendModule)
      make[DBConnectionProvider].from(dbProvider)
    (injector, module)

  def getInjectorAndModule[F[_], A](datasourceUri: String): (Injector[Identity], ModuleDef) =
    getInjectorAndModule(() => NamedDB(datasourceUri))

  def getInjectorAndModule[F[_], A](connection: Connection, autoclose: Boolean): (Injector[Identity], ModuleDef) =
    getInjectorAndModule(() => DB(connection).autoClose(autoclose))

  def getInjectorAndModule[F[_], A](dataSource: DataSource, autoclose: Boolean): (Injector[Identity], ModuleDef) =
    getInjectorAndModule(() => DB(dataSource.getConnection).autoClose(autoclose))

  def produceRun[F[_], A](dbProvider: DBConnectionProvider): Functoid[Identity[A]] => Identity[A] =
    val (injector, module) = getInjectorAndModule(dbProvider)
    injector.produceRun(module)

  def produceRun[F[_], A](datasourceUri: String): Functoid[Identity[A]] => Identity[A] =
    produceRun(() => NamedDB(datasourceUri))

  def produceRun[F[_], A](connection: Connection, autoclose: Boolean): Functoid[Identity[A]] => Identity[A] =
    produceRun(() => DB(connection).autoClose(autoclose))

  def produceRun[F[_], A](dataSource: DataSource, autoclose: Boolean): Functoid[Identity[A]] => Identity[A] =
    produceRun(() => DB(dataSource.getConnection).autoClose(autoclose))
