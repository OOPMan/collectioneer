package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.cli.{Config, Subconfig, Subject, Verb}
import com.oopman.collectioneer.plugins.CLIPlugin
import io.circe.Json
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}

case class EmbeddedPostgresDatabaseBackendPluginCLIPluginConfig() extends Subconfig()

object EmbeddedPostgresDatabaseBackendPluginCLIPlugin extends CLIPlugin:
  def getName: String = "Embedded PostgreSQL Database Backend CLI"

  def getShortName: String = "embeddedpostgresbackendcli"

  def getVersion: String = "master"

  def getDefaultSubconfig: Subconfig = EmbeddedPostgresDatabaseBackendPluginCLIPluginConfig()

  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Config => Json, List[OParser[_, Config]])] =
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
