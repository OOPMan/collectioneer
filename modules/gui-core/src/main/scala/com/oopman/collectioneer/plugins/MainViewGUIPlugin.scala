package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.db.traits.dao.raw.CollectionDAO
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.Collection as RawCollection
import scalafx.scene.control.TreeCell

import java.util.UUID

trait MainViewGUIPlugin extends GUIPlugin:
  def getCollectionsListTreeViewCellFactory(collection: Collection): Option[(TreeCell[Collection], Collection) => Unit]
  def getRawChildCollections(collection: Collection, collectionDAO: CollectionDAO): Option[List[Collection]]
  def getPropertyPKsForInflation(collection: Collection): Option[Seq[UUID]]
