package com.oopman.collectioneer.gui

import com.oopman.collectioneer.{CoreCollections, CoreProperties}
import com.oopman.collectioneer.db.Injection
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits
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
      val text = collection.propertyValues
        .find(pv => pv.property.pk == CoreProperties.name.property.pk)
        .flatMap(pv => pv.textValues.headOption)
        .getOrElse(collection.pk.toString)
      cell.text = text
    }
    // TODO: In the future, SelctionMode.Multiple may be required
    selectionModel().selectionMode = SelectionMode.Single
    selectionModel().selectedItem.onChange {
      (observableValue, oldItem, newItem) =>
        refreshChildren(newItem) // TODO: We should probably only refresh if children is empty or data has changed...
        // TODO: Render Collection detail into Collection detail pane
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
    Injection.produceRun(Some(config)) {
      (collectionDAO: traits.dao.raw.CollectionDAO, projectedCollectionDAO: traits.dao.projected.CollectionDAO) =>
      // TODO: This could be hooked into plugins to allow default parameters or something...?
      // TODO: Query and update should happen in a worker, we should probably show a progress spinner somewhere
      val collections = collectionDAO.getAllMatchingConstraints(parentCollectionPKs = Some(Seq(treeItem.value.value.pk)))
      val projectedCollections = projectedCollectionDAO.inflateRawCollections(collections)
      val treeItems = projectedCollections.map(projectedCollection => TreeItem(projectedCollection))
      treeItem.children = treeItems
    }
    // If treeItem is rootTreeViewItem we load all top-level collections
    // Otherwise we load all children of the input item

  // TODO: Use config to load database
  def getNode =
    refreshChildren()
    splitPane
