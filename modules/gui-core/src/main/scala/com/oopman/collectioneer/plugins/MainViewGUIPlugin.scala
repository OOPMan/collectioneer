package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.db.traits.dao.raw.CollectionDAO
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.Collection as RawCollection
import scalafx.scene.control.TreeCell

import java.util.UUID

trait MainViewGUIPlugin extends GUIPlugin:
  def canGetCollectionsListTreeViewCellFactory(collection: Collection): Boolean
  def getCollectionsListTreeViewCellFactory(collection: Collection): (TreeCell[Collection], Collection) => Unit
  def canGetRawChildCollections(collection: Collection): Boolean
  def getRawChildCollections(collection: Collection, collectionDAO: CollectionDAO): Seq[RawCollection]
  def canGetPropertyPKsForInflation(collection: Collection): Boolean
  def getPropertyPKsForInflation(collection: Collection): Seq[UUID]
