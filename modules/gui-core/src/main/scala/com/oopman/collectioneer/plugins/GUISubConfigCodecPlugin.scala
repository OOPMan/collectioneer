package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.gui.GUISubConfig
import com.oopman.collectioneer.{Injection, Plugin}
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

trait GUISubConfigCodecPlugin extends Plugin:
  def subConfigKey: String
  def canEncode(subConfig: GUISubConfig): Boolean
  def encode(subConfig: GUISubConfig): Json
  def getDefaultGUISubConfig: GUISubConfig
  def decoder: Decoder[GUISubConfig]

object GUISubConfigCodecPlugin:
  // TODO: This will fail if there are no subconfig plugins, make it a method that returns an empty map if it fails
  private val guiSubConfigCodecPluginMap: Map[String, GUISubConfigCodecPlugin] = Injection.produceRun(withConfig = false) {
    (plugins: Set[GUISubConfigCodecPlugin]) => plugins.map(plugin => plugin.subConfigKey -> plugin).toMap
  }

  private object GUISubConfigEncoder extends Encoder[GUISubConfig]:
    final def apply(subConfig: GUISubConfig): Json =
      val plugin = guiSubConfigCodecPluginMap.values.find(_.canEncode(subConfig))
      val result = plugin.map(plugin => plugin.encode(subConfig)).getOrElse(Json.Null)
      // TODO: Log if result is Json.Null
      result

  private object GUISubConfigDecoder extends Decoder[Map[String, GUISubConfig]]:
    final def apply(cursor: HCursor): Result[Map[String, GUISubConfig]] =
      val keys = cursor.keys.getOrElse(Nil)
      val mapTupleOptions =
        for
          key <- keys
          plugin <- guiSubConfigCodecPluginMap.get(key)
        yield
          implicit val decoder: Decoder[GUISubConfig] = plugin.decoder
          val valueCursor = cursor.downField(key)
          val subConfigResult = valueCursor.as[GUISubConfig]
          // TODO: Log failures
          subConfigResult.map(subConfig => key -> subConfig).toOption
      //          key -> valueCursor.as[SubConfig].getOrElse(plugin.getDefaultSubConfig) // TODO: Remove when we're happy with this whole thing
      Right(mapTupleOptions.flatten.toMap)

  def getDefaultGUISubConfigs: Map[String, GUISubConfig] =
    for (key, plugin) <- guiSubConfigCodecPluginMap
      yield key -> plugin.getDefaultGUISubConfig

  implicit val encodeGUISubConfig: Encoder[GUISubConfig] = GUISubConfigEncoder
  implicit val decodeGUISubConfigsMap: Decoder[Map[String, GUISubConfig]] = GUISubConfigDecoder