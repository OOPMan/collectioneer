package com.oopman.collectioneer.gui

import com.oopman.collectioneer.CoreCollections
import com.oopman.collectioneer.db.Injection
import com.oopman.collectioneer.db.entity.projected.Collection
import com.oopman.collectioneer.db.traits.dao.raw.CollectionDAO
import scalafx.scene.control.{ScrollPane, SelectionMode, SplitPane, TreeItem, TreeView}
import scalafx.scene.layout.Region
import scalafx.Includes.*

class MainView(val config: GUIConfig):

  private lazy val rootCollection = CoreCollections.root.collection

  private lazy val rootTreeViewItem = TreeItem(rootCollection)

  private lazy val collectionsListTreeView = new TreeView[Collection]:
    root = rootTreeViewItem
    showRoot = true
    cellFactory = (cell, collection) => {
      // TODO: Hook this into plugins to allow for custom formatting of items based on their properties with fallback default to render the name or UUID
      cell.text = collection.pk.toString
    }
    // TODO: In the future, SelctionMode.Multiple may be required
    selectionModel().selectionMode = SelectionMode.Single
    selectionModel().selectedItem.onChange {
      (observableValue, oldItem, newItem) =>
        refreshChildren(newItem) // TODO: We should probably only refresh if children is empty or data has changed...
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
      // TODO: This could be hooked into plugins to allow default parameters or something...?
      val collections = collectionDAO.getAllMatchingConstraints(
        parentCollectionPKs = Some(Seq(treeItem.value.value.pk))
      )
      // TODO: TreeItems need to call refreshChildren on themselves when they are selected
      val treeItems = collections.map(c => TreeItem(Collection(pk = c.pk)))  // Temporary hack for quick testing
      treeItem.children = treeItems
    }
    // If treeItem is rootTreeViewItem we load all top-level collections
    // Otherwise we load all children of the input item

  // TODO: Use config to load database
  def getNode =
    refreshChildren()
    splitPane
