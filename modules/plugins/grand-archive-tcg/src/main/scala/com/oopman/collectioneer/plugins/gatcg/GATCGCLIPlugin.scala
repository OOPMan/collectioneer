package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.cli.{Config, Subconfig, Subject, Verb}
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.db.traits.entity.raw.{Property, PropertyType}
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

  override def getMigrationLocations(databaseBackend: DatabaseBackend): Seq[String] = Nil

  override def getDefaultSubconfig: Subconfig = GATCGPluginConfig()

  override def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Config => Json, List[OParser[_, Config]])] =
    List(
      (Verb.imprt, Subject.database, importDatabase, List()),
    )

  def importDatabase(config: Config) =
    // TODO: Create GA-specific properties if they don't exist using an UPSERT
//    val propertiesDAO = new PropertyDAO(config.datasourceUri)
//    val properties = GATCGProperties.values.toList.map(_.property)
//    propertiesDAO.createOrUpdateProperties(properties)
    // TODO: Replace with a real response
    "Something".asJson

object GATCGCLIPlugin extends PluginDef:
  many[CLIPlugin].add(GATCGCLIPlugin())
  many[Plugin].add(GATCGCLIPlugin())
