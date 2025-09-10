package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.plugins.*
import com.oopman.collectioneer.plugins.gatcg.gui.detailview.GATCGDetailViewGUIPlugin
import com.oopman.collectioneer.plugins.gatcg.gui.mainview.GATCGMainViewGUIPlugin
import com.oopman.collectioneer.plugins.gatcg.gui.menu.GATCGPluginsMenuGUIPlugin
import izumi.distage.plugins.PluginDef

object GATCGUIPluginDef extends PluginDef:
  many[PluginsMenuGUIPlugin]
    .add[GATCGPluginsMenuGUIPlugin]
  many[GUISubConfigCodecPlugin]
    .add[GATCGGUISubConfigCodecPlugin]
  many[DetailViewGUIPlugin]
    .add[GATCGDetailViewGUIPlugin]
  many[MainViewGUIPlugin]
    .add[GATCGMainViewGUIPlugin]
  many[GUIPlugin]
    .add[GATCGMainViewGUIPlugin]
    .add[GATCGDetailViewGUIPlugin]
  many[Plugin]
    .add[GATCGDetailViewGUIPlugin]
    .add[GATCGMainViewGUIPlugin]
    .add[GATCGGUISubConfigCodecPlugin]

