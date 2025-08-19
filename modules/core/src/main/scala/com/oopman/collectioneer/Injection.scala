package com.oopman.collectioneer

import com.oopman.collectioneer.db.DatabaseBackendPlugin
import distage.*
import distage.plugins.PluginConfig
import izumi.distage.plugins.load.PluginLoader
import izumi.fundamentals.platform.functional.Identity


class Injection(inputModuleDef: ModuleDef = Injection.emptyModule, withConfig: Boolean = true):
  def getInjectorAndModule[F[_], A]: (Injector[Identity], ModuleDef) =
    Injection.getInjectorAndModule(inputModuleDef, withConfig)

  def produceRun[F[_], A]: Functoid[Identity[A]] => Identity[A] =
    Injection.produceRun(inputModuleDef, withConfig)

  def produce[T: Tag]: Identity[T] =
    Injection.produce[T](inputModuleDef, withConfig)


object Injection:
  private val pluginConfig: PluginConfig = PluginConfig.cached("com.oopman.collectioneer.plugins")
  private val pluginModules: ModuleBase = PluginLoader().load(pluginConfig).result.merge
  private val injector: Injector[Identity] = Injector()
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
  
  def produce[T : Tag](inputModuleDef: ModuleDef = emptyModule, withConfig: Boolean = true): Identity[T] =
    produceRun(inputModuleDef, withConfig)((producedInstance: T) => producedInstance)

  def apply(inputModuleDef: ModuleDef = emptyModule, withConfig: Boolean = true) =
    new Injection(inputModuleDef, withConfig)
