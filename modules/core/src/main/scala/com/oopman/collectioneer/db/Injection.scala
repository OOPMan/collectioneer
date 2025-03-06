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
  private object emptyModule extends ModuleDef
  private object dummyConfig extends Config:
    val datasourceUri: Option[String] = None

  def getInjectorAndModule[F[_], A](configOption: Option[Config] = None, inputModule: ModuleDef = emptyModule): (Injector[Identity], ModuleDef) =
    val config = configOption.getOrElse(dummyConfig)
    val baseModule = new ModuleDef:
      make[Config].from(config)
      include(pluginModules)
    val databaseBackendPluginOption: Option[DatabaseBackendPlugin] = injector.produceRun(baseModule) {
      (databaseBackendPlugins: Set[DatabaseBackendPlugin]) => databaseBackendPlugins.find(_.compatibleWithDatasourceUri)
    }
    val finalModule = new ModuleDef:
      for (databaseBackendPlugin <- databaseBackendPluginOption) make[DatabaseBackendPlugin].from(databaseBackendPlugin)
      include(baseModule)
      for (databaseBackendPlugin <- databaseBackendPluginOption) include(databaseBackendPlugin.getDatabaseBackendModule)
      include(inputModule)
    (injector, finalModule)

  def produceRun[F[_], A](configOption: Option[Config] = None, inputModule: ModuleDef = emptyModule): Functoid[Identity[A]] => Identity[A] =
    val (injector, module) = getInjectorAndModule(configOption, inputModule)
    injector.produceRun(module)
