package com.oopman.collectioneer.gui

import com.oopman.collectioneer.{CoreCollections, CoreProperties}
import com.oopman.collectioneer.db.{Injection, SortDirection, traits}
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import scalafx.scene.control.{ProgressIndicator, ScrollPane, SelectionMode, SplitPane, TreeItem, TreeView}
import scalafx.scene.layout.Region
import scalafx.Includes.*
import scalafx.concurrent.Task


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
    val originalGraphic = treeItem.getGraphic
    treeItem.graphic = new ProgressIndicator
    val worker = Task {
      val collections = Injection.produceRun(Some(config)) {
        (collectionDAO: traits.dao.raw.CollectionDAO, projectedCollectionDAO: traits.dao.projected.CollectionDAO) =>
          // TODO: This could be hooked into plugins to allow default parameters or something...?
          // TODO: Query and update should happen in a worker, we should probably show a progress spinner somewhere
          val collections = collectionDAO.getAllMatchingConstraints(
            parentCollectionPKs = Some(Seq(treeItem.value.value.pk)),
            sortProperties = Seq(CoreProperties.name.property -> SortDirection.Asc)
          )
          // TODO: We should probably only inflate the CoreProperties.name property
          // TODO: Decision of which properties to inflate should depend on a plugin
          val projectedCollections = projectedCollectionDAO.inflateRawCollections(
            collections,
            propertyPKs = Seq(CoreProperties.name.property.pk)
          )
          projectedCollections
//          val treeItems = projectedCollections.map(projectedCollection => TreeItem(projectedCollection))
//          treeItem.children = treeItems
      }
      collections
    }
    worker.onSucceeded = { e =>
      val collections = worker.getValue
      val treeItems = collections.map(projectedCollection => TreeItem(projectedCollection))
      treeItem.graphic = originalGraphic
      treeItem.children = treeItems
    }
    // TODO: Handle failure
    val thread = new Thread(worker)
    thread.setDaemon(true)
    thread.start()

  // TODO: Use config to load database
  def getNode =
    refreshChildren()
    splitPane
