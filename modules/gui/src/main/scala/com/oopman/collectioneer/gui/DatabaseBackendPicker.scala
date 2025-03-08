package com.oopman.collectioneer.gui

import com.oopman.collectioneer.db.Injection
import com.oopman.collectioneer.plugins.DatabaseBackendGUIPlugin
import scalafx.scene.Node
import scalafx.scene.control.ChoiceBox
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.text.Text
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.util.StringConverter

object DatabaseBackendPicker:
  lazy val plugins: Set[DatabaseBackendGUIPlugin] =
    Injection.produceRun(inputModule = CollectioneerGUI.collectioneerGUIModule) {
      (databaseBackendGUIPlugins: Set[DatabaseBackendGUIPlugin]) => databaseBackendGUIPlugins
    }
  lazy val pluginMap: Map[String, DatabaseBackendGUIPlugin] = plugins.map(plugin => (plugin.getUIName, plugin)).toMap

  lazy val pluginChoiceBoxLabel = new Text:
    text = "Database Backend Plugin:"

  lazy val pluginChoiceBox: ChoiceBox[DatabaseBackendGUIPlugin] = new ChoiceBox:
    items = ObservableBuffer.from(plugins)
    converter = StringConverter(
      fromStringFunction = pluginMap.apply,
      toStringFunction = (plugin: DatabaseBackendGUIPlugin) => Option(plugin).map(_.getUIName).getOrElse("")
    )
    onAction = event => getSelectedPlugin.map(plugin => layout.center = plugin.getNode)

  lazy val layout = new BorderPane:
    top = new HBox:
      children = Seq(pluginChoiceBoxLabel, pluginChoiceBox)

  def getSelectedPlugin: Option[DatabaseBackendGUIPlugin] = Option(pluginChoiceBox.selectionModel().getSelectedItem)

  def getNode: Node = layout

  def getConfig(config: GUIConfig = GUIConfig()): GUIConfig =
    getSelectedPlugin
      .map(plugin => config.copy(datasourceUri = Some(plugin.getDatasourceURI)))
      .getOrElse(config.copy(datasourceUri = None))
