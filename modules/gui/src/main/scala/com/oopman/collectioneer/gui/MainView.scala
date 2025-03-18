package com.oopman.collectioneer.gui

import com.oopman.collectioneer.{CoreCollections, CoreProperties, Injection}
import com.oopman.collectioneer.db.{SortDirection, traits}
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.Collection as RawCollection
import com.oopman.collectioneer.plugins.MainViewGUIPlugin
import scalafx.scene.control.{ProgressIndicator, ScrollPane, SelectionMode, SplitPane, TreeCell, TreeItem, TreeView}
import scalafx.scene.layout.Region
import scalafx.Includes.*
import scalafx.concurrent.Task
import scalafx.scene.Node


class MainView(val config: GUIConfig):
  private lazy val plugins: Set[MainViewGUIPlugin] =
    Injection.produceRun(Some(config), CollectioneerGUI.collectioneerGUIModule) {
      (mainViewGUIPlugins: Set[MainViewGUIPlugin]) => mainViewGUIPlugins
    }

  private lazy val rootCollection = CoreCollections.root.collection

  private lazy val rootTreeViewItem = TreeItem(rootCollection)

  private lazy val collectionsListTreeView = new TreeView[Collection]:
    root = rootTreeViewItem
    showRoot = true
    cellFactory = (cell, collection) => {
      val cellFactory: (TreeCell[Collection], Collection) => Unit = plugins
        .find(plugin => plugin.canGetCollectionsListTreeViewCellFactory(collection))
        .map(plugin => plugin.getCollectionsListTreeViewCellFactory(collection))
        .getOrElse((cell, collection) => {
          val text = collection.propertyValues
            .find(pv => pv.property.pk == CoreProperties.name.property.pk)
            .flatMap(pv => pv.textValues.headOption)
            .getOrElse(collection.pk.toString)
          cell.text = text
        })
      cellFactory(cell, collection)
    }
    // TODO: In the future, SelctionMode.Multiple may be required
    selectionModel().selectionMode = SelectionMode.Single
    selectionModel().selectedItem.onChange {
      (observableValue, oldItem, newItem) =>
        if newItem.getChildren.size() == 0 then refreshChildren(newItem)
        // TODO: Render Collection detail into Collection detail pane in https://github.com/OOPMan/collectioneer/issues/34
    }

  // TODO: Implement this in https://github.com/OOPMan/collectioneer/issues/34
  private lazy val collectionDetailView = new Region

  private lazy val splitPane = new SplitPane:
    items.addAll(collectionsListScrollPane, collectionDetailViewScrollPane)

  private lazy val collectionsListScrollPane = new ScrollPane:
    content = collectionsListTreeView
    fitToWidth = true
    fitToHeight = true

  private lazy val collectionDetailViewScrollPane = new ScrollPane:
    content = collectionDetailView

  def refreshChildren(treeItem: TreeItem[Collection] = rootTreeViewItem): Unit =
    val worker = Task {
      val collections = Injection.produceRun(Some(config)) {
        (collectionDAO: traits.dao.raw.CollectionDAO, projectedCollectionDAO: traits.dao.projected.CollectionDAO) =>
          val collection = treeItem.getValue
          val collections: List[RawCollection] = plugins
            .find(plugin => plugin.canGetRawChildCollections(collection))
            .map(plugin => plugin.getRawChildCollections(collection, collectionDAO))
            .getOrElse(collectionDAO.getAllMatchingConstraints(
              parentCollectionPKs = Some(Seq(collection.pk)),
              sortProperties = Seq(CoreProperties.name.property -> SortDirection.Asc)
            ))
          val propertyPKs = plugins
            .find(plugin => plugin.canGetPropertyPKsForInflation(collection))
            .map(plugin => plugin.getPropertyPKsForInflation(collection))
            .getOrElse(Seq(CoreProperties.name.property.pk))
          projectedCollectionDAO.inflateRawCollections(
            collections,
            propertyPKs = propertyPKs
          )
      }
      collections
    }
    worker.onSucceeded = { e =>
      val collections = worker.getValue
      val treeItems = collections.map(projectedCollection => TreeItem(projectedCollection))
      treeItem.children = treeItems
    }
    // TODO: Handle failure
    val thread = new Thread(worker)
    thread.setDaemon(true)
    thread.start()

  def getNode: Node =
    refreshChildren()
    splitPane
