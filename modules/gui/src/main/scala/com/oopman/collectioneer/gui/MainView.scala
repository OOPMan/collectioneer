package com.oopman.collectioneer.gui

import com.oopman.collectioneer.db.entity.projected.Collection
import scalafx.scene.control.{ScrollPane, SplitPane, TreeView}
import scalafx.scene.layout.Region

object MainView:
  private lazy val splitPane = new SplitPane

  private lazy val collectionsListScrollPane = new ScrollPane

  private lazy val collectionDetailViewScrollPane = new ScrollPane

  private lazy val collectionsListTreeView = new TreeView[Collection]

  // TODO: Implement this in https://github.com/OOPMan/collectioneer/issues/34
  private lazy val collectionDetailView = new Region

  // TODO: Use config to load database
  def getNode(config: GUIConfig) = splitPane
