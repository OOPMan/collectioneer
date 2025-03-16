package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.plugins.{GUIPlugin, MainViewGUIPlugin}
import izumi.distage.plugins.PluginDef

object GATCGUIPluginDef extends PluginDef:
  many[MainViewGUIPlugin]
    .add[GATCGMainViewGUIPlugin]
  many[GUIPlugin]
    .add[GATCGMainViewGUIPlugin]
  many[Plugin]
    .add[GATCGMainViewGUIPlugin]
