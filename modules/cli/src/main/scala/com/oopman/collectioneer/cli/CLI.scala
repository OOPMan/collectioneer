package com.oopman.collectioneer.cli

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.cli.actions
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.plugins.CLIPlugin
import distage.plugins.PluginConfig
import distage.{Injector, ModuleBase, ModuleDef}
import io.circe.Json
import io.circe.syntax.*
import io.circe.yaml.*
import izumi.distage.plugins.load.PluginLoader
import izumi.fundamentals.platform.functional.Identity
import scalikejdbc.{GlobalSettings, LoggingSQLAndTimeSettings}
import scopt.{OParser, OParserBuilder}

import java.util.UUID

type Action = Config => Json
type ActionListItem = (Verb, Subject, Option[String], Action, List[OParser[?, Config]])
type PluginNameActionListItemMap = Map[Option[String], ActionListItem]
type SubjectMap = Map[Subject, PluginNameActionListItemMap]
type ActionMap = Map[Verb, SubjectMap]

def cliSafeName(name: String): String = name
  .toLowerCase
  .replaceAll("[^a-z- ]", "")
  .replaceAll("\\s+", "-")

/**
 * TODO: Document this property so I don't forget what it does
 * @param builder
 * @param particle
 * @param subject
 * @param pluginNameActionListItemMap
 * @return
 */
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
  val oParserItems = if pluginNames.nonEmpty then pluginNameOpt :: actionListItems.flatMap(_._5) else actionListItems.flatMap(_._5)
  builder.cmd(subject.name)
    .text(subject.help.getOrElse(verb, ""))
    .action((_, config) => config.copy(subject = Some(subject)))
    .children(oParserItems*)

def generateVerbCmd
(builder: OParserBuilder[Config])
(verb: Verb, subjectsMap: SubjectMap) =
  val oParserItems = subjectsMap.map(generateSubjectCmds(builder, verb)).toList
  builder.cmd(verb.name)
    .text(verb.help)
    .action((_, config) => config.copy(verb = Some(verb)))
    .children(oParserItems*)

object CLI:
  val builder: OParserBuilder[Config] = OParser.builder[Config]
  val pluginConfig: PluginConfig = PluginConfig.cached("com.oopman.collectioneer.plugins")
  val pluginModules: ModuleBase = PluginLoader().load(pluginConfig).result.merge
  private object fakeModule extends ModuleDef:
    include(pluginModules)
    make[com.oopman.collectioneer.Config].from(Config())
  val plugins: List[CLIPlugin] = Injector[Identity]()
    .produceRun(fakeModule)((cliPlugins: Set[CLIPlugin]) => cliPlugins).toList
//  val plugins: List[CLIPlugin] =
//    try Injector()
//      .produceGet[Set[CLIPlugin]](pluginModules)
//      .unsafeGet()
//      .toList
//    catch case e: ProvisioningException => Nil
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
  val propertyValueQueryArgs: OParser[String, Config] = builder.arg[String]("<PropertyValueQueries>...")
    .unbounded()
    .optional()
    .action((propertyValueQuery, config) => config.copy(propertyValueQueries = config.propertyValueQueries.map(propertyValueQuery :: _).orElse(Some(propertyValueQuery :: Nil))))
    .text("Add PropertyValue filters. Filter format is <UUID4><Operator><Value> (e.g. a82f8e14-0f5b-467f-a85d-0810537a41c5>=10). Multiple filters are ANDed together")
  val baseActions: List[ActionListItem] = List(
    (Verb.list, Subject.collections, None, actions.list.Collections.listCollectionsAction, List(deletedOpt, virtualOp, propertyValueQueryArgs)),
    (Verb.list, Subject.properties, None, actions.list.Properties.listPropertiesAction, List(deletedOpt, propertyValueQueryArgs)),
    (Verb.list, Subject.plugins, None, _ => plugins.map(plugin => s"${plugin.getName} (${plugin.getVersion})").asJson, List()),
    (Verb.get, Subject.collections, None, actions.get.Collections.getCollections, List(uuidArgs)),
    (Verb.get, Subject.properties, None, actions.get.Properties.getProperties, List(uuidArgs)),
    (Verb.imprt, Subject.database, None, actions.imprt.Database.importDatabase, List(importDatasourceUriArgs))
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
    import builder.*

    val oParserItems: List[OParser[?, Config]] = List(
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
        .action((datasourceUri, config) => config.copy(datasourceUri = Some(datasourceUri))),
      opt[String]("datasourceUsername")
        .text("Datasource username")
        .action((datasourceUsername, config) => config.copy(datasourceUsername = datasourceUsername)),
      opt[String]("datasourcePassword")
        .text("Datasource password")
        .action((datasourcePassword, config) => config.copy(datasourcePassword = datasourcePassword)),
    ) ++ actionsMap.map(generateVerbCmd(builder)).toList
    OParser.sequence(
      programName("Collectioneer CLI"),
      oParserItems*
    )

  def main(args: Array[String]): Unit =
    OParser.parse(parser, args, Config(subconfigs = pluginSubconfigs)) match
      case Some(config) =>
        // Configure ScalikeJDBC
        GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
          enabled = config.debug,
          singleLineMode = !config.verbose
        )
//        try {
          Injection.produceRun(Some(config)) { (databaseBackendPlugin: DatabaseBackendPlugin) => databaseBackendPlugin.startUp() }
          // Process CLI arguments
          val action = for {
            verb <- config.verb
            subject <- config.subject
          } yield actionsMap(verb)(subject)(config.usePlugin)._4
          val result = action.map(_(config)).getOrElse(Map[String, String]().asJson)
          val resultString = config.outputFormat match
            case OutputFormat.json => result.spaces2SortKeys
            case OutputFormat.yaml => io.circe.yaml.Printer(preserveOrder = true).pretty(result)
          println(resultString)
//        } finally {
//          Injection.produceRun(config) { (databaseBackendPlugin: DatabaseBackendPlugin) => databaseBackendPlugin.shutDown() }
//        }
      case _ =>
