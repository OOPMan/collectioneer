package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.{SubConfig, WithTag}
import com.oopman.collectioneer.gui.GUISubConfig
import distage.ModuleDef

case class GATCGSubConfig
(
  imagePath: String = (os.pwd / "gatcg"/ "images").toString
) extends GUISubConfig, WithTag[GATCGSubConfig]:
  def getModuleDefForSubConfig: ModuleDef =
    val subConfig = this
    new ModuleDef:
      many[SubConfig].add(subConfig)
      many[GUISubConfig].add(subConfig)
      make[GATCGSubConfig].from(subConfig)
  
