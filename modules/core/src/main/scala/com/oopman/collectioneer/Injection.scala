package com.oopman.collectioneer

import com.oopman.collectioneer.Config
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import distage.*
import distage.plugins.PluginConfig
import izumi.distage.plugins.load.PluginLoader
import izumi.fundamentals.platform.functional.Identity


object Injection:
  protected val pluginConfig: PluginConfig = PluginConfig.cached("com.oopman.collectioneer.plugins")
  protected val pluginModules: ModuleBase = PluginLoader().load(pluginConfig).result.merge
  protected val injector: Injector[Identity] = Injector()
  private object emptyModule extends ModuleDef
  var baseModuleDef: ModuleDef = emptyModule

  def getInjectorAndModule[F[_], A](inputModuleDef: ModuleDef = emptyModule, withConfig: Boolean = true): (Injector[Identity], ModuleDef) =
    val bootstrapModuleDef = new ModuleDef:
      include(baseModuleDef)
      include(pluginModules)
    val configModuleDef =
      if withConfig
      then injector.produceRun(bootstrapModuleDef) {
        (configManager: ConfigManager) => configManager.getModuleDefForConfig
      }
      else emptyModule
    val databaseBackendPluginOption =
      try
        injector.produceRun(bootstrapModuleDef ++ configModuleDef) {
          (databaseBackendPlugins: Set[DatabaseBackendPlugin]) => databaseBackendPlugins.find(_.compatibleWithDatasourceUri)
        }
      catch
        case _: Throwable => None
    val outputModuleDef = new ModuleDef:
      include(bootstrapModuleDef)
      include(configModuleDef)
      for (databaseBackendPlugin <- databaseBackendPluginOption)
        make[DatabaseBackendPlugin].from(databaseBackendPlugin)
        include(databaseBackendPlugin.getDatabaseBackendModule)
      include(inputModuleDef)
    (injector, outputModuleDef)


  def produceRun[F[_], A](inputModuleDef: ModuleDef = emptyModule, withConfig: Boolean = true): Functoid[Identity[A]] => Identity[A] =
    val (injector, module) = getInjectorAndModule(inputModuleDef, withConfig)
    injector.produceRun(module)
