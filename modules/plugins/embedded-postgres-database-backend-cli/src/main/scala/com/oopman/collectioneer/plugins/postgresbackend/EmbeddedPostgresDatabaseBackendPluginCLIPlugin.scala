package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.SubConfig
import com.oopman.collectioneer.cli.{CLIConfig, CLISubConfig, Subject, Verb}
import com.oopman.collectioneer.plugins.CLIPlugin
import distage.ModuleDef
import io.circe.Json
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}

case class EmbeddedPostgresDatabaseBackendPluginCLIPluginConfig() extends CLISubConfig:
  def getModuleDefForSubConfig: ModuleDef = 
    val subConfig = this
    new ModuleDef:
      many[SubConfig].add(subConfig)
      many[CLISubConfig].add(subConfig)
      make[EmbeddedPostgresDatabaseBackendPluginCLIPluginConfig].from(subConfig)


object EmbeddedPostgresDatabaseBackendPluginCLIPlugin extends CLIPlugin:
  def getName: String = "Embedded PostgreSQL Database Backend CLI"

  def getShortName: String = "embeddedpostgresbackendcli"

  def getVersion: String = "master"

  def getDefaultSubConfig: CLISubConfig = EmbeddedPostgresDatabaseBackendPluginCLIPluginConfig()

  def getActions(builder: OParserBuilder[CLIConfig]): List[(Verb, Subject, CLIConfig => Json, List[OParser[?, CLIConfig]])] =
    List(
      (Verb.get, Subject.database, getDatabase, List())
    )

  def getDatabase(config: CLIConfig) =
    import io.circe.*
    import io.circe.syntax.*
    println()
    println("Press any key to exit")
    scala.io.StdIn.readLine()
    "Something".asJson

object EmbeddedPostgresDatabaseBackendPluginCLIPluginDef extends PluginDef:
  many[CLIPlugin]
    .add(EmbeddedPostgresDatabaseBackendPluginCLIPlugin)
