package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.db.traits.entity.projected.Collection
import scalafx.scene.control.Tab

trait DetailViewGUIPlugin extends GUIPlugin:
  def canRenderCollection(collection: Collection): Boolean
  def generateCollectionRenderer(collection: Collection): Collection => Option[Tab]
