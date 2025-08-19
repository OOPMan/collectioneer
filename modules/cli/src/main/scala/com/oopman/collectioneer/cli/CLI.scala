package com.oopman.collectioneer.cli

import com.oopman.collectioneer.{ConfigManager, Injection}
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import distage.ModuleDef
import io.circe.syntax.*
import scalikejdbc.{GlobalSettings, LoggingSQLAndTimeSettings}

object CLI:

  def main(args: Array[String]): Unit =
    val cliConfigManager = new CLIConfigManager(args)
    Injection.baseModuleDef = new ModuleDef:
      make[ConfigManager].from(cliConfigManager)
      make[CLIConfigManager].from(cliConfigManager)

    Injection.produceRun() { (config: CLIConfig, databaseBackendPlugin: DatabaseBackendPlugin) =>
      GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
        enabled = config.debug,
        singleLineMode = !config.verbose
      )
      // TODO: Database backend startUp should be lazy, current approach blocks calls that don't use DB when DB isn't active
      databaseBackendPlugin.startUp();
      val actionOption = cliConfigManager.getActionOption
      val result = actionOption.map(_(config)).getOrElse(Map[String, String]().asJson)
      val resultString = config.outputFormat match
        case OutputFormat.json => result.spaces2SortKeys
        case OutputFormat.yaml => io.circe.yaml.Printer(preserveOrder = true).pretty(result)
      println(resultString)
    }

  