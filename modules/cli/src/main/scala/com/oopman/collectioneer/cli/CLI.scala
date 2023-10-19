package com.oopman.collectioneer.cli

import com.oopman.collectioneer.cli.{Config, Subject, Verb}
import com.oopman.collectioneer.db.migrations.executeMigrations
import com.oopman.collectioneer.db.dao.CollectionsDAO
import com.oopman.collectioneer.db.entity.Collection
import org.flywaydb.core.Flyway
import org.h2.jdbcx.JdbcDataSource
import scopt.{OParser, OParserBuilder}
import scalikejdbc.*

import java.io.File
import java.util.UUID
import io.circe.generic.auto.*
import io.circe.syntax.*
import cats.syntax.either.*
import com.oopman.collectioneer.cli.actions.list.{getCollections, listCollections, listPlugins, listProperties}
import com.oopman.collectioneer.plugins.CLIPlugin
import distage.{Injector, ModuleBase}
import distage.plugins.PluginConfig
import io.circe.Json
import io.circe.yaml.*
import io.circe.yaml.syntax.*
import izumi.distage.model.exceptions.runtime.ProvisioningException
import izumi.distage.plugins.load.PluginLoader

val actionsMap: Map[(Option[Verb], Option[Subject]), Config => Json] = Map(
  (Some(Verb.list), Some(Subject.collections)) -> listCollections,
  (Some(Verb.list), Some(Subject.properties)) -> listProperties,
//  (Some(Verb.list), Some(Subject.plugins)) -> listPlugins,
  (Some(Verb.get), Some(Subject.collections)) -> getCollections,
)

type Action = Config => Json
type ActionListItem = (Verb, Subject, Option[String], Action, List[OParser[_, Config]])
type PluginNameActionListItemMap = Map[Option[String], ActionListItem]
type SubjectMap = Map[Subject, PluginNameActionListItemMap]
type ActionMap = Map[Verb, SubjectMap]

def cliSafeName(name: String): String = name
  .toLowerCase
  .replaceAll("[^a-z- ]", "")
  .replaceAll("\\s+", "-")

def generateSubjectCmds
(builder: OParserBuilder[Config], verb: Verb)
(subject: Subject, pluginNameActionListItemMap: PluginNameActionListItemMap) =
  val pluginNames = pluginNameActionListItemMap.keySet.flatten
  val pluginNamesString = pluginNames.mkString("|")
  val actionListItems = pluginNameActionListItemMap.values.toList
  val pluginNameOpt = builder.opt[String]("use-plugin")
    .text(s"Specify whether to use a specific plugin. Valid options: " + pluginNamesString)
    .maxOccurs(1)
    .optional()
    .action((pluginName, config) => config.copy(usePlugin = Some(cliSafeName(pluginName))))
    .validate(pluginName =>
      if pluginNames.contains(pluginName) then builder.success
      else builder.failure("Invalid plugin name. Valid options: " + pluginNamesString))
  val oParserItems = pluginNameOpt :: actionListItems.flatMap(_._5)
  builder.cmd(subject.name)
    .text(subject.help.getOrElse(verb, ""))
    .action((_, config) => config.copy(subject = Some(subject)))
    .children(oParserItems: _*)

def generateVerbCmd
(builder: OParserBuilder[Config])
(verb: Verb, subjectsMap: SubjectMap) =
  val oParserItems = subjectsMap.map(generateSubjectCmds(builder, verb)).toList
  builder.cmd(verb.name)
    .text(verb.help)
    .action((_, config) => config.copy(verb = Some(verb)))
    .children(oParserItems: _*)

object CLI:
  val builder: OParserBuilder[Config] = OParser.builder[Config]
  val pluginConfig: PluginConfig = PluginConfig.cached("com.oopman.collectioneer.plugins")
  val pluginModules: ModuleBase = PluginLoader().load(pluginConfig).result.merge
  val plugins: List[CLIPlugin] =
    try Injector()
      .produceGet[Set[CLIPlugin]](pluginModules)
      .unsafeGet()
      .toList
    catch case e: ProvisioningException => Nil
  val pluginActions: List[ActionListItem] = plugins
    .flatMap(plugin => plugin
      .getActions(builder)
      .map((verb, subject, action, oparserItems) => (verb, subject, Some(cliSafeName(plugin.getShortName)), action, oparserItems))
    )
  val pluginSubconfigs: Map[String, Subconfig] = plugins.map(p => p.getShortName -> p.getDefaultSubconfig).toMap
  val uuidArgs: OParser[String, Config] = builder.arg[String]("<UUID>...")
    .unbounded()
    .required()
    .action((uuid, config) => config.copy(uuids = UUID.fromString(uuid) :: config.uuids))
    .text("One or more UUIDs")
  val importDatasourceUriArgs: OParser[String, Config] = builder.arg[String]("<URI>...")
    .unbounded()
    .required()
    .action((importDatasourceUri, config) => config.copy(importDatasourceUris = importDatasourceUri :: config.importDatasourceUris))
    .text("One or more Datasource URIs")
  val deletedOpt: OParser[Boolean, Config] = builder.opt[Boolean]("deleted")
    .optional()
    .action((deleted, config) => config.copy(deleted = Some(deleted)))
    .text("Filter by deleted field value")
  val virtualOp: OParser[Boolean, Config] = builder.opt[Boolean]("virtual")
    .optional()
    .action((virtual, config) => config.copy(virtual = Some(virtual)))
    .text("Filter by virtual field value")
  val baseActions: List[ActionListItem] = List(
    (Verb.list, Subject.collections, None, listCollections, List(deletedOpt, virtualOp)),
    (Verb.list, Subject.properties, None, listProperties, List(deletedOpt)),
    (Verb.list, Subject.plugins, None, _ => plugins.map(plugin => s"${plugin.getName} (${plugin.getVersion})").asJson, List()),
    (Verb.get, Subject.collections, None, getCollections, List(uuidArgs)),
    (Verb.imprt, Subject.database, None, importDatabase, List(importDatasourceUriArgs))
    // TODO: Add more
  )
  val actionsMap: ActionMap = baseActions
    .concat(pluginActions)
    .groupBy(_._1)
    .map((verb: Verb, actionList: List[ActionListItem]) =>
      verb -> actionList
        .groupBy(_._2)
        .map((subject: Subject, actionList: List[ActionListItem]) =>
          subject -> actionList
            .groupBy(_._3)
            .map((pluginNameOption: Option[String], actionList: List[ActionListItem]) =>
              pluginNameOption -> actionList.head))
    )

  val parser: OParser[Unit, Config] =
    import builder._

    val oParserItems: List[OParser[_, Config]] = List(
      head("collectioneer-cli", "master"),
      help("help").text("Coming soon..."),
      opt[Unit]("json")
        .action((_, config) => config.copy(outputFormat = OutputFormat.json))
        .text("Enable JSON output"),
      opt[Unit]("verbose")
        .action((_, config) => config.copy(verbose = true))
        .text("Enable verbose output"),
      opt[Unit]("debug")
        .action((_, config) => config.copy(debug = true))
        .text("Enable debugging output"),
      opt[String]("datasourceUri")
        .text("JDBC URI")
        // TODO: Add validator
        .action((datasourceUri, config) => config.copy(datasourceUri = datasourceUri)),
      opt[String]("datasourceUsername")
        .text("Datasource username")
        .action((datasourceUsername, config) => config.copy(datasourceUsername = datasourceUsername)),
      opt[String]("datasourcePassword")
        .text("Datasource password")
        .action((datasourcePassword, config) => config.copy(datasourcePassword = datasourcePassword)),
    ) ++ actionsMap.map(generateVerbCmd(builder)).toList
    OParser.sequence(
      programName("Collectioneer CLI"),
      oParserItems: _*
    )

  def main(args: Array[String]): Unit =
    OParser.parse(parser, args, Config(subconfigs = pluginSubconfigs)) match
      case Some(config) =>
        // Setup DataSoure
        val dataSource = new JdbcDataSource();
        dataSource.setURL(config.datasourceUri)
        dataSource.setUser(config.datasourceUsername)
        dataSource.setPassword(config.datasourcePassword)
        // Execute Flyway Migrations
        executeMigrations(dataSource)
        // Configure ScalikeJDBC
        GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
          enabled = config.debug,
          singleLineMode = !config.verbose
        )
        ConnectionPool.singleton(new DataSourceConnectionPool(dataSource))
        ConnectionPool.add(config.datasourceUri, new DataSourceConnectionPool(dataSource))
        // Process CLI arguments
        val action = for {
          verb <- config.verb
          subject <- config.subject
        } yield actionsMap(verb)(subject)(config.usePlugin)._4
        val result = action.map(_(config)).getOrElse(Map[String, String]().asJson)
        val resultString = config.outputFormat match
            case OutputFormat.json => result.spaces2SortKeys
            case OutputFormat.yaml => result.asYaml.spaces2
        println(resultString)
      case _ =>




