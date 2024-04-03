package com.oopman.collectioneer.db

import com.oopman.collectioneer.Config
import distage.*
import distage.plugins.PluginConfig
import izumi.distage.plugins.load.PluginLoader
import izumi.fundamentals.platform.functional.Identity


object Injection:
  protected val pluginConfig: PluginConfig = PluginConfig.cached("com.oopman.collectioneer.plugins")
  protected val pluginModules: ModuleBase = PluginLoader().load(pluginConfig).result.merge
  protected val injector: Injector[Identity] = Injector()

  def getInjectorAndModule[F[_], A](config: Config): (Injector[Identity], ModuleDef) =
    val baseModule = new ModuleDef:
      make[Config].from(config)
      include(pluginModules)
    val databaseBackendPlugin: DatabaseBackendPlugin = injector.produceRun(baseModule) {
      (databaseBackendPlugins: Set[DatabaseBackendPlugin]) => databaseBackendPlugins.filter(_.compatibleWithDatasourceUri).head
    }
    val finalModule = new ModuleDef:
      make[DatabaseBackendPlugin].from(databaseBackendPlugin)
      include(baseModule)
      include(databaseBackendPlugin.getDatabaseBackendModule)
    (injector, finalModule)

  def produceRun[F[_], A](config: Config): Functoid[Identity[A]] => Identity[A] =
    val (injector, module) = getInjectorAndModule(config)
    injector.produceRun(module)
