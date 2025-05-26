package com.oopman.collectioneer.gui

import com.oopman.collectioneer.{Config, ConfigManager}
import com.oopman.collectioneer.plugins.GUISubConfigCodecPlugin
import distage.ModuleDef
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

// TODO: Make a Configuration trait that defines the configModuleProvider and maybe some other stuff. Implement versions for GUI and CLI
// TODO: This probably needs to be moved to GUI Core?
// TODO: Thread safety
// TODO: Provide a Distage Module exposing the GUIConfig along with all sub-configs
object GUIConfigManager extends ConfigManager:
  private val defaultRootPath = os.pwd
  private val defaultConfigFilePath = defaultRootPath / "collectioneer.yaml"
  private val defaultGUIConfig = GUIConfig(subConfigs = GUISubConfigCodecPlugin.getDefaultGUISubConfigs)
  private var configOption: Option[GUIConfig] = None

  private def loadAndParseConfiguration(path: os.Path = defaultConfigFilePath) =
    if !os.exists(path)
    then defaultGUIConfig
    else
      import GUISubConfigCodecPlugin.*
      import io.circe.yaml
      implicit val decodeGUIConfig: Decoder[GUIConfig] = deriveDecoder[GUIConfig]
      // TODO: Handle read failure
      val data: String = os.read(defaultConfigFilePath)
      // TODO: Log parser failures
      val json = yaml.parser.parse(data)
      // TODO: Log coercion failures
      val config = json.flatMap(_.as[GUIConfig])
      config.getOrElse(defaultGUIConfig)

  def getConfig: GUIConfig =
    configOption match
      case Some(guiConfig) => guiConfig
      case None =>
        val guiConfig = loadAndParseConfiguration()
        configOption = Some(guiConfig)
        guiConfig

  def getConfig_=(newConfig: GUIConfig): GUIConfig =
    configOption = Some(newConfig)
    newConfig

  def loadConfig(path: os.Path = defaultConfigFilePath): GUIConfig =
    val guiConfig = loadAndParseConfiguration(path)
    configOption = Some(guiConfig)
    guiConfig

  def saveConfig(path: os.Path): Boolean =
    // TODO: Implement
    ???

  def getModuleDefForConfig: ModuleDef = 
    val config = this.getConfig
    new ModuleDef:
      make[Config].from(config)
      make[GUIConfig].from(config)
      for (subConfig <- config.subConfigs.values) do include(subConfig.getModuleDefForSubConfig)
