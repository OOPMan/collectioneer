package com.oopman.collectioneer.gui

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.plugins.DatabaseBackendGUIPlugin
import com.oopman.collectioneer.gui.GUIConfigManager
import scalafx.scene.Node
import scalafx.scene.control.{Button, ChoiceBox, Label, ProgressIndicator}
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.concurrent.Task
import scalafx.util.StringConverter

object DatabaseBackendPicker:
  private lazy val plugins: Set[DatabaseBackendGUIPlugin] =
    Injection.produceRun() {
      (databaseBackendGUIPlugins: Set[DatabaseBackendGUIPlugin]) => databaseBackendGUIPlugins
    }
  private lazy val pluginMap: Map[String, DatabaseBackendGUIPlugin] = plugins.map(plugin => (plugin.getUIName, plugin)).toMap

  private lazy val pluginChoiceBoxLabel = new Label:
    text = "Database Backend Plugin"

  private lazy val pluginChoiceBox: ChoiceBox[DatabaseBackendGUIPlugin] = new ChoiceBox:
    items = ObservableBuffer.from(plugins)
    converter = StringConverter(
      fromStringFunction = pluginMap.apply,
      toStringFunction = (plugin: DatabaseBackendGUIPlugin) => Option(plugin).map(_.getUIName).getOrElse("")
    )
    onAction = event => getSelectedPlugin.map(plugin => layout.center = plugin.getNode)

  private lazy val backButton: Button = new Button:
    text = "Back"

  private lazy val connectButton: Button = new Button:
    text = "Connect"
    onAction = { e =>
      backButton.disable = true
      connectButton.disable = true
      progressIndicator.visible = true
      DatabaseBackendPicker.updateConfigDatasourceUri()
      val worker = Task {
        Injection.produceRun() {
          (databaseBackendPlugin: DatabaseBackendPlugin) => databaseBackendPlugin.startUp()
        }
      }
      worker.onSucceeded = { e =>
        progressIndicator.visible = false
        CollectioneerGUI.showMainView()
      }
      worker.onFailed = { e =>
        // TODO: Display an error message
        progressIndicator.visible = false
        connectButton.disable = false
        backButton.disable = false
      }
      val thread = new Thread(worker)
      thread.setDaemon(true)
      thread.start()
    }

  private lazy val progressIndicator = new ProgressIndicator:
    visible = false

  private lazy val layout = new BorderPane:
    top = new HBox:
      children = Seq(pluginChoiceBoxLabel, pluginChoiceBox)
    bottom = new HBox:
      children = Seq(backButton, connectButton, progressIndicator)

  private def getSelectedPlugin: Option[DatabaseBackendGUIPlugin] = Option(pluginChoiceBox.selectionModel().getSelectedItem)

  def getNode(backButtonVisible: Boolean = true): Node =
    backButton.visible = backButtonVisible
    backButton.disable = false
    connectButton.disable = false
    progressIndicator.visible = false
    layout

  private def updateConfigDatasourceUri(): Unit =
    Injection.produceRun() { (configManager: GUIConfigManager) =>
      val config = configManager.getConfig
      val updatedConfig = getSelectedPlugin
        .map(plugin => config.copy(datasourceUri = Some(plugin.getDatasourceURI)))
        .getOrElse(config.copy(datasourceUri = None))
      configManager.updateConfig(updatedConfig)
    }

