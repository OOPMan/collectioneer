package com.oopman.collectioneer.gui

import com.oopman.collectioneer.plugins.GUISubConfigCodecPlugin
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

object Configuration:
  private val defaultRootPath = os.pwd
  private val defaultConfigFilePath = defaultRootPath / "collectioneer.yaml"
  private val defaultGUIConfig = GUIConfig(subConfigs = GUISubConfigCodecPlugin.getDefaultGUISubConfigs)
  private var config: Option[GUIConfig] = None

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

  def getConfiguration: GUIConfig =
    config match
      case Some(config) => config
      case None =>
        val guiConfig = loadAndParseConfiguration()
        config = Some(guiConfig)
        guiConfig

  def updateConfiguration(newConfig: GUIConfig): GUIConfig =
    config = Some(newConfig)
    newConfig

  def loadConfiguration(path: os.Path): GUIConfig =
    val guiConfig = loadAndParseConfiguration(path)
    config = Some(guiConfig)
    guiConfig

  def saveConfiguration(path: os.Path): Boolean =
    // TODO: Implement
    ???


