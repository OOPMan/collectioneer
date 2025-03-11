package com.oopman.collectioneer.plugins

import scalafx.scene.Node

trait DatabaseBackendGUIPlugin extends GUIPlugin:
  def getUIName: String
  def getNode: Node
  def getDatasourceURI: String
