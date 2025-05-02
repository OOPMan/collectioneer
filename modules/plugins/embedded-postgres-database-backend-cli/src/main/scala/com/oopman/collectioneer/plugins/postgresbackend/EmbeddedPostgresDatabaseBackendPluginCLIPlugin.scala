package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.cli.{CLISubConfig, Config, Subject, Verb}
import com.oopman.collectioneer.plugins.CLIPlugin
import io.circe.Json
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}

case class EmbeddedPostgresDatabaseBackendPluginCLIPluginConfig() extends CLISubConfig

object EmbeddedPostgresDatabaseBackendPluginCLIPlugin extends CLIPlugin:
  def getName: String = "Embedded PostgreSQL Database Backend CLI"

  def getShortName: String = "embeddedpostgresbackendcli"

  def getVersion: String = "master"

  def getDefaultSubConfig: CLISubConfig = EmbeddedPostgresDatabaseBackendPluginCLIPluginConfig()

  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Config => Json, List[OParser[?, Config]])] =
    List(
      (Verb.get, Subject.database, getDatabase, List())
    )

  def getDatabase(config: Config) =
    import io.circe.*
    import io.circe.syntax.*
    println()
    println("Press any key to exit")
    scala.io.StdIn.readLine()
    "Something".asJson

object EmbeddedPostgresDatabaseBackendPluginCLIPluginDef extends PluginDef:
  many[CLIPlugin]
    .add(EmbeddedPostgresDatabaseBackendPluginCLIPlugin)
