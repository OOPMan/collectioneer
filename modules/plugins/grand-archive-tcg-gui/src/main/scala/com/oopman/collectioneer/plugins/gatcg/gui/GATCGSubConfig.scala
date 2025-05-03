package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.gui.GUISubConfig

case class GATCGSubConfig
(
  imagePath: String = (os.pwd / "gatcg"/ "images").toString
) extends GUISubConfig
