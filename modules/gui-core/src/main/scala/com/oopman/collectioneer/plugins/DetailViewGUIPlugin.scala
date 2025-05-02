package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.db.traits.entity.projected.Collection
import scalafx.scene.Node

trait DetailViewGUIPlugin extends GUIPlugin:
  def canRenderCollection(collection: Collection): Boolean
  def renderCollection(collection: Collection): Node
