package com.oopman.collectioneer.gui

import com.oopman.collectioneer.{Config, SubConfig}

trait GUISubConfig extends SubConfig

case class GUIConfig
(
  datasourceUri: Option[String] = None,
  subConfigs: Map[String, GUISubConfig] = Map.empty
) extends Config
