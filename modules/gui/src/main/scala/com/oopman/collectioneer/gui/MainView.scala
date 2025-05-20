package com.oopman.collectioneer.gui

import com.oopman.collectioneer.{CoreCollections, CoreProperties, Injection, given}
import com.oopman.collectioneer.db.{SortDirection, traits}
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.Collection as RawCollection
import com.oopman.collectioneer.plugins.{DetailViewGUIPlugin, MainViewGUIPlugin}
import scalafx.scene.control.{ScrollPane, SelectionMode, SplitPane, TabPane, TreeCell, TreeItem, TreeView}
import scalafx.Includes.*
import scalafx.concurrent.Task
import scalafx.scene.Node
import scalafx.scene.control.TabPane.TabClosingPolicy


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
            .find((property, pv) => property == CoreProperties.name)
            .flatMap((property, pv) => pv.textValues.headOption)
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
        refreshDetailView(newItem)
        // TODO: Render Collection detail into Collection detail pane in https://github.com/OOPMan/collectioneer/issues/34
    }

  private lazy val collectionDetailView = new TabPane:
    tabClosingPolicy = TabClosingPolicy.Unavailable

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
          val collections: Seq[RawCollection] = plugins
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
          projectedCollectionDAO.inflateRawCollections(collections, propertyPKs = propertyPKs)
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

  def refreshDetailView(treeItem: TreeItem[Collection] = rootTreeViewItem): Unit =
    val worker = Task {
      Injection.produceRun(Some(config)) {
        (projectedCollectionDAO: traits.dao.projected.CollectionDAO, plugins: Set[DetailViewGUIPlugin]) =>
          val collectionToInflate = treeItem.getValue
          val collection = projectedCollectionDAO
            .inflateRawCollections(collectionToInflate :: Nil)
            .headOption
            .getOrElse(collectionToInflate)
          val renderers = plugins
            .filter(_.canRenderCollection(collection))
            .map(_.generateCollectionRenderer(collection))
          // TODO: In the event that renderers is empty, we should return a default renderer that simply renders the propertyValue data in a tetual form
          collection -> renderers
      }
    }
    worker.onSucceeded = { e =>
      val (collection, renderers) = worker.getValue
      val tabs = renderers.map(renderer => renderer(collection))
      collectionDetailView.tabs = tabs.toSeq
    }
    // TODO: Handle failure
    val thread = new Thread(worker)
    thread.setDaemon(true)
    thread.start()

  def getNode: Node =
    refreshChildren()
    splitPane
