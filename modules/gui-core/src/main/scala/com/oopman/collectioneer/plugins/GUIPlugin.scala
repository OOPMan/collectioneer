package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.Plugin
import distage.Id
import scalafx.stage.Stage

trait GUIPlugin(val stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage")) extends Plugin
