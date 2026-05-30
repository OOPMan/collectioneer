package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.gui.GUISubConfig
import com.oopman.collectioneer.plugins.GUISubConfigCodecPlugin
import io.circe.{Decoder, Json}

class GATCGGUISubConfigCodecPlugin extends GUISubConfigCodecPlugin:
  private val defaultSubConfig = GATCGSubConfig()
  val subConfigKey: String = defaultSubConfig.getKeyForSubConfig

  def canEncode(subConfig: GUISubConfig): Boolean =
    subConfig.isInstanceOf[GATCGSubConfig]

  def encode(subConfig: GUISubConfig): Json =
    // TODO: Implement
    Json.Null

  def decoder: Decoder[GUISubConfig] =
    import cats.syntax.functor.*
    import io.circe.*
    import io.circe.generic.semiauto.*
    deriveDecoder[GATCGSubConfig].widen

  def getDefaultGUISubConfig: GUISubConfig = defaultSubConfig

  def getName: String = "GATCG SubConfig Codec Plugin"

  override def getShortName: String = "GATCGSubConfigCodec"

  def getVersion: String = "master"
