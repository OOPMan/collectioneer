package com.oopman.collectioneer.gui

import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.Collection as RawCollection
import com.oopman.collectioneer.db.{SortDirection, traits}
import com.oopman.collectioneer.plugins.{DetailViewGUIPlugin, MainViewGUIPlugin}
import com.oopman.collectioneer.{CoreCollections, CoreProperties, Injection}
import com.typesafe.scalalogging.LazyLogging
import izumi.distage.model.exceptions.runtime.ProvisioningException
import scalafx.Includes.*
import scalafx.concurrent.Task
import scalafx.scene.Node
import scalafx.scene.control.*
import scalafx.scene.control.TabPane.TabClosingPolicy
import scalafx.scene.layout.BorderPane


class MainView extends LazyLogging:
  private lazy val plugins: Set[MainViewGUIPlugin] =
    try Injection.produce[Set[MainViewGUIPlugin]]()
    catch case e: ProvisioningException =>
      logger.warn("Failed to produce any MainViewGUIPlugin instances")
      Set.empty

  private lazy val rootCollection = CoreCollections.root.collection

  private lazy val rootTreeViewItem = TreeItem(rootCollection)

  private lazy val collectionsListTreeView: TreeView[Collection] = new TreeView[Collection]:
    styleClass += CollectioneerGUICSS.listView
    root = rootTreeViewItem
    showRoot = true
    cellFactory = (cell, collection) => {
      val cellFactory: (TreeCell[Collection], Collection) => Unit = plugins
        .find(plugin => plugin.canGetCollectionsListTreeViewCellFactory(collection))
        .map(plugin => plugin.getCollectionsListTreeViewCellFactory(collection))
        .getOrElse((cell, collection) => {
          val text = collection.propertyValues
            .find((property, pv) => property == CoreProperties.name)
            .flatMap((property, pv) => pv.stringValues.headOption)
            .getOrElse(collection.pk.toString)
          cell.text = text
        })
      cellFactory(cell, collection)
    }
    // TODO: In the future, SelctionMode.Multiple may be required
    selectionModel().selectionMode = SelectionMode.Single
    selectionModel().selectedItem.onChange {
      (observableValue, oldItem, newItem) =>
        val worker = Task {
          Injection.produceRun() {
            (collectionDAO: traits.dao.projected.CollectionDAO) =>
              val collection = newItem.getValue
              val collections = collectionDAO.inflateRawCollections(collection :: Nil)
              collections.headOption.getOrElse(collection)
          }
        }
        worker.onSucceeded = {e =>
          newItem.value = worker.getValue
          if newItem.getChildren.size() == 0 then refreshChildren(newItem)
          refreshDetailView(newItem)
          createCollectionButton.disable = false
        }
        // TODO: Handle failure
        val thread = new Thread(worker)
        thread.setDaemon(true)
        thread.start()
    }

  private lazy val collectionDetailView = new TabPane:
    styleClass += CollectioneerGUICSS.detailView
    tabClosingPolicy = TabClosingPolicy.Unavailable

  private lazy val splitPane = new SplitPane:
    styleClass += CollectioneerGUICSS.mainView
    items.addAll(collectionsListScrollPane, collectionDetailViewScrollPane)

  private lazy val collectionsListScrollPane = new ScrollPane:
    styleClass += CollectioneerGUICSS.mainViewLeftSide
    content = collectionsListTreeView
    fitToWidth = true
    fitToHeight = true

  private lazy val collectionDetailViewScrollPane = new ScrollPane:
    styleClass += CollectioneerGUICSS.mainViewRightSide
    content = collectionDetailView
    fitToWidth = true
    fitToHeight = true

  private lazy val createCollectionButton: Button = new Button:
    disable = true
    text = "Create Collection"
    // TODO: Make it an icon button
    onAction = { e =>
      collectionsListTreeView.disable = true
      disable = true
      val selectedCollection = collectionsListTreeView.selectionModel().getSelectedItem.getValue
      collectionDetailViewScrollPane.content = new CreateCollectionVBox(selectedCollection.pk):
        def onDone(collection: Option[Collection] = None): Unit =
          // TODO: Add newly created Collection to TreeView or refresh parent?
          collectionDetailViewScrollPane.content = collectionDetailView
          createCollectionButton.disable = false
          collectionsListTreeView.disable = false
    }

  private lazy val toolbar = new ToolBar:
    // TODO: Style to introduce spacing around content
    content = createCollectionButton :: Nil

  private lazy val borderLayout = new BorderPane:
    // TODO: Style to introduce spacing around top and center
    top = toolbar
    center = splitPane

  def refreshChildren(treeItem: TreeItem[Collection] = rootTreeViewItem): Unit =
    val collection = treeItem.getValue
    val worker = Task {
      val collections = Injection.produceRun() {
        (collectionDAO: traits.dao.raw.CollectionDAO, projectedCollectionDAO: traits.dao.projected.CollectionDAO) =>
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
    val collection = treeItem.getValue
    val worker = Task {
      val detailViewGUIPlugins =
        try Injection.produce[Set[DetailViewGUIPlugin]]()
        catch case e: ProvisioningException =>
          logger.warn("Failed to produce any DetailViewGUIPlugin instances")
          Set.empty
      detailViewGUIPlugins
        .filter(_.canRenderCollection(collection))
        .map(_.generateCollectionRenderer(collection))
    }
    worker.onSucceeded = { e =>
      val renderers = worker.getValue
      val tabs =
        for
          renderer <- renderers
          tab <- renderer(collection)
        yield tab
      collectionDetailView.tabs = tabs.toSeq
    }
    worker.onFailed = { e =>
      // TODO: Log exceptions
      worker.getException.printStackTrace()
    }
    // TODO: Handle failure
    val thread = new Thread(worker)
    thread.setDaemon(true)
    thread.start()

  def getNode: Node =
    refreshChildren()
    borderLayout