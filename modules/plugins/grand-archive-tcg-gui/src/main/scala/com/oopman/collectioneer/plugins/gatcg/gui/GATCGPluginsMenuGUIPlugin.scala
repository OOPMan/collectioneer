package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.plugins.{GUIPlugin, PluginsMenuGUIPlugin}
import distage.Id
import scalafx.scene.control.{Menu, MenuItem}
import scalafx.stage.Stage

class GATCGPluginsMenuGUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), PluginsMenuGUIPlugin:
  def getMenu: Menu = new Menu("Grand Archive TCG"):
    items = List(
      new MenuItem("Download Dataset"),
      new MenuItem("Import Dataset"),
      new MenuItem("Download Images"),
    )

  override def getName: String = "GATCG Plugins Menu GUI Plugin"

  override def getShortName: String = "GATCGPluginsMenuGUI"

  override def getVersion: String = "master"

