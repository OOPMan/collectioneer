package com.oopman.collectioneer.plugins.gatcg.gui.controls.text

import scalafx.scene.text.{Text, Font => SFXFont, FontWeight, FontPosture}

trait Font(size: Double = 12,
           weight: FontWeight=FontWeight.Normal,
           posture: FontPosture = FontPosture.Regular,
           family: String = "System") extends Text:
  font = SFXFont(family, weight, posture, size)
