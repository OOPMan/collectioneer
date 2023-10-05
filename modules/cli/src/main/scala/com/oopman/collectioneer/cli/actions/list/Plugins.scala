package com.oopman.collectioneer.cli.actions.list

import com.oopman.collectioneer.cli.Config
import com.oopman.collectioneer.plugins.CLIPlugin
import distage.Injector
import izumi.distage.plugins.PluginConfig
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import izumi.distage.plugins.load.PluginLoader

def listPlugins(config: Config): Json =
  val pluginConfig = PluginConfig.cached(
    "com.oopman.collectioneer.plugins" :: Nil
  )
  val appModules = PluginLoader().load(pluginConfig).result.merge
  Injector()
    .produceGet[Set[CLIPlugin]](appModules)
    .unsafeGet()
    .map(plugin => s"${plugin.getName} (${plugin.getVersion})")
    .asJson


