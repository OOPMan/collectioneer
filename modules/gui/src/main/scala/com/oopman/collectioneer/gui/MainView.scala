package com.oopman.collectioneer.gui

import com.oopman.collectioneer.db.Injection
import com.oopman.collectioneer.db.entity.projected.Collection
import com.oopman.collectioneer.db.traits.dao.raw.CollectionDAO
import scalafx.scene.control.{ScrollPane, SplitPane, TreeItem, TreeView}
import scalafx.scene.layout.Region
import scalafx.Includes.*

class MainView(val config: GUIConfig):

  private lazy val rootCollection = Collection()

  private lazy val rootTreeViewItem = TreeItem(rootCollection)

  private lazy val collectionsListTreeView = new TreeView[Collection]:
    root = rootTreeViewItem
    showRoot = true
    cellFactory = (cell, collection) => {
      cell.text = collection.pk.toString
    }

  // TODO: Implement this in https://github.com/OOPMan/collectioneer/issues/34
  private lazy val collectionDetailView = new Region

  private lazy val splitPane = new SplitPane:
    items.addAll(collectionsListScrollPane, collectionDetailViewScrollPane)

  private lazy val collectionsListScrollPane = new ScrollPane:
    content = collectionsListTreeView


  private lazy val collectionDetailViewScrollPane = new ScrollPane:
    content = collectionDetailView

  def refreshChildren(treeItem: TreeItem[Collection] = rootTreeViewItem): Unit =
    Injection.produceRun(Some(config)) { (collectionDAO: CollectionDAO) =>
      val parentCollectionPKs =
        if treeItem.equals(rootTreeViewItem)
        then Nil
        else Seq(treeItem.value.value.pk)
      val collections = collectionDAO.getAllMatchingConstraints(parentCollectionPKs = Some(parentCollectionPKs))
      val treeItems = collections.map(c => TreeItem(Collection(pk = c.pk)))  // Temporary hack for quick testing
      treeItem.children = treeItems
      println(s"Collections length: ${collections.size}")
    }
    // If treeItem is rootTreeViewItem we load all top-level collections
    // Otherwise we load all children of the input item

  // TODO: Use config to load database
  def getNode =
    refreshChildren()
    splitPane
