package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.SubConfig
import com.oopman.collectioneer.gui.GUISubConfig
import distage.ModuleDef

case class GATCGSubConfig
(
  imagePath: String = (os.pwd / "gatcg"/ "images").toString
) extends GUISubConfig:
  def getModuleDefForSubConfig: ModuleDef =
    val subConfig = this
    new ModuleDef:
      many[SubConfig].add(subConfig)
      many[GUISubConfig].add(subConfig)
      make[GATCGSubConfig].from(subConfig)
  
