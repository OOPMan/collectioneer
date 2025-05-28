package com.oopman.collectioneer.cli

import com.oopman.collectioneer
import com.oopman.collectioneer.{Config, ConfigManager, Injection}
import com.oopman.collectioneer.plugins.CLIPlugin
import distage.ModuleDef
import io.circe.Json
import io.circe.syntax.*
import scopt.{OParser, OParserBuilder}

import java.util.UUID

type Action = CLIConfig => Json
type ActionListItem = (Verb, Subject, Option[String], Action, List[OParser[?, CLIConfig]])
type PluginNameActionListItemMap = Map[Option[String], ActionListItem]
type SubjectMap = Map[Subject, PluginNameActionListItemMap]
type ActionMap = Map[Verb, SubjectMap]

class CLIConfigManager(val args: Seq[String]) extends ConfigManager:
  private def cliSafeName(name: String): String = name
    .toLowerCase
    .replaceAll("[^a-z- ]", "")
    .replaceAll("\\s+", "-")

  /**
   * TODO: Document this property so I don't forget what it does
   *
   * @param builder
   * @param particle
   * @param subject
   * @param pluginNameActionListItemMap
   * @return
   */
  private def generateSubjectCmds
  (builder: OParserBuilder[CLIConfig], verb: Verb)
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
      .children(oParserItems *)

  private def generateVerbCmd
  (builder: OParserBuilder[CLIConfig])
  (verb: Verb, subjectsMap: SubjectMap) =
    val oParserItems = subjectsMap.map(generateSubjectCmds(builder, verb)).toList
    builder.cmd(verb.name)
      .text(verb.help)
      .action((_, config) => config.copy(verb = Some(verb)))
      .children(oParserItems *)
    
  private val builder: OParserBuilder[CLIConfig] = OParser.builder[CLIConfig]

  private val plugins: List[CLIPlugin] = Injection.produceRun(withConfig = false) {
    (cliPlugins: Set[CLIPlugin]) => cliPlugins.toList
  }

  private val pluginActions: List[ActionListItem] = plugins
    .flatMap(plugin => plugin
      .getActions(builder)
      .map((verb, subject, action, oparserItems) => (verb, subject, Some(cliSafeName(plugin.getShortName)), action, oparserItems))
    )
  // TODO: Populate default sub-configs differently
  private val pluginSubconfigs: Map[String, CLISubConfig] =
    plugins.map(plugin => plugin.getShortName -> plugin.getDefaultSubConfig).toMap
  private val uuidArgs: OParser[String, CLIConfig] = builder.arg[String]("<UUID>...")
    .unbounded()
    .required()
    .action((uuid, config) => config.copy(uuids = UUID.fromString(uuid) :: config.uuids))
    .text("One or more UUIDs")
  private val importDatasourceUriArgs: OParser[String, CLIConfig] = builder.arg[String]("<URI>...")
    .unbounded()
    .required()
    .action((importDatasourceUri, config) => config.copy(importDatasourceUris = importDatasourceUri :: config.importDatasourceUris))
    .text("One or more Datasource URIs")
  private val deletedOpt: OParser[Boolean, CLIConfig] = builder.opt[Boolean]("deleted")
    .optional()
    .action((deleted, config) => config.copy(deleted = Some(deleted)))
    .text("Filter by deleted field value")
  private val virtualOp: OParser[Boolean, CLIConfig] = builder.opt[Boolean]("virtual")
    .optional()
    .action((virtual, config) => config.copy(virtual = Some(virtual)))
    .text("Filter by virtual field value")
  private val propertyValueQueryArgs: OParser[String, CLIConfig] = builder.arg[String]("<PropertyValueQueries>...")
    .unbounded()
    .optional()
    .action((propertyValueQuery, config) => config.copy(propertyValueQueries = config.propertyValueQueries.map(propertyValueQuery :: _).orElse(Some(propertyValueQuery :: Nil))))
    .text("Add PropertyValue filters. Filter format is <UUID4><Operator><Value> (e.g. a82f8e14-0f5b-467f-a85d-0810537a41c5>=10). Multiple filters are ANDed together")
  private val baseActions: List[ActionListItem] = List(
    (Verb.list, Subject.collections, None, actions.list.Collections.listCollectionsAction, List(deletedOpt, virtualOp, propertyValueQueryArgs)),
    (Verb.list, Subject.properties, None, actions.list.Properties.listPropertiesAction, List(deletedOpt, propertyValueQueryArgs)),
    (Verb.list, Subject.plugins, None, _ => plugins.map(plugin => s"${plugin.getName} (${plugin.getVersion})").asJson, List()),
    (Verb.get, Subject.collections, None, actions.get.Collections.getCollections, List(uuidArgs)),
    (Verb.get, Subject.properties, None, actions.get.Properties.getProperties, List(uuidArgs)),
    (Verb.imprt, Subject.database, None, actions.imprt.Database.importDatabase, List(importDatasourceUriArgs))
    // TODO: Add more
  )
  private val actionsMap: ActionMap = baseActions
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

  private val parser: OParser[Unit, CLIConfig] =
    import builder.*

    val oParserItems: List[OParser[?, CLIConfig]] = List(
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
      oParserItems *
    )

  private val initialConfig = CLIConfig(subConfigs = pluginSubconfigs)
  private val config: CLIConfig = OParser.parse(parser, args, initialConfig).getOrElse(initialConfig)
  
  inline def getConfig: CLIConfig = config
  
  def getModuleDefForConfig: ModuleDef =
    new ModuleDef:
      make[Config].from(getConfig)
      make[CLIConfig].from(getConfig)
      for (subConfig <- getConfig.subConfigs.values) do include(subConfig.getModuleDefForSubConfig)

  def getActionOption: Option[Action] =
    for {
      verb <- config.verb
      subject <- config.subject
      subjectMap <- actionsMap.get(verb)
      pluginNameActionListItemMap <- subjectMap.get(subject)
      (_, _, _, action, _) <- pluginNameActionListItemMap.get(config.usePlugin)
    } yield action

