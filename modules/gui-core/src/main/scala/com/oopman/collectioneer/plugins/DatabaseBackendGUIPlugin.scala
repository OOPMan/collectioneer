package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.db.DatabaseBackendPlugin
import scalafx.scene.Node

trait DatabaseBackendGUIPlugin extends GUIPlugin:
  def getUIName: String
  def getNode: Node
  def getDatabaseBackendPlugin: DatabaseBackendPlugin
