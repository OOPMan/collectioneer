package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.cli.{Action, Config, Subconfig, Subject, Verb}
import com.oopman.collectioneer.plugins.CLIPlugin
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

class GATCGCLIPlugin extends CLIPlugin:
  override def getName: String = "Grand Archive TCG"

  override def getShortName: String = "GATCG"

  override def getVersion: String = "master"

  override def getDefaultSubconfig: Subconfig = GATCGPluginConfig()

  override def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Action, List[OParser[_, Config]])] =
    List(
      (Verb.imprt, Subject.database, importDatabase, List()),
    )

  def importDatabase(config: Config) = "Something".asJson

object GATCGCLIPlugin extends PluginDef:
  many[CLIPlugin].add(GATCGCLIPlugin())
