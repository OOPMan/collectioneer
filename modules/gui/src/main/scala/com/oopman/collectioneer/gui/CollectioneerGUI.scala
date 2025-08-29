package com.oopman.collectioneer.gui

import com.oopman.collectioneer.plugins.PluginsMenuGUIPlugin
import com.oopman.collectioneer.{ConfigManager, Injection}
import distage.ModuleDef
import fr.brouillard.oss.cssfx.CSSFX
import izumi.distage.model.exceptions.runtime.ProvisioningException
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.layout.BorderPane
import scalafx.stage.Stage

import scala.language.implicitConversions
import scala.util.Try

object CollectioneerGUI extends JFXApp3:
  // TODO: Create lazy vals for menu items
  // TODO: Implement handlers for menu items
  // TODO: Tie menu-bar width to stage width somehow
  lazy val pluginsMenuGUIPlugins: Set[PluginsMenuGUIPlugin] = 
    try Injection.produce[Set[PluginsMenuGUIPlugin]]()
    catch case e: ProvisioningException => Set.empty // TODO: Log this error
    
  lazy val pluginsMenu = new Menu("Plugins")

  lazy val borderPane = new BorderPane:
    styleClass += CollectioneerGUICSS.root
    top = new MenuBar:
      useSystemMenuBar = true
      minWidth = 1024
      menus = List(
        new Menu("File") {
          items = List(
            new MenuItem("Open"),
            new MenuItem("Exit")
          )
        },
        pluginsMenu,
        new Menu("Help") {
          items = List(
            new MenuItem("About")
          )
        }
      )
    
  lazy val primaryStage = new JFXApp3.PrimaryStage:
    title = "Collectioneer"
    scene = new Scene(1024, 768):
      root = borderPane

  override def start(): Unit =
    stage = primaryStage
    // TODO: Use parameters to determine if we need to load configuration from a separate file
    val guiConfigManager = new GUIConfigManager
    Injection.baseModuleDef = new ModuleDef:
      make[ConfigManager].from(guiConfigManager)
      make[GUIConfigManager].from(guiConfigManager)
      make[Stage].named("com.oopman.collectioneer.plugins.GUIPlugin.stage").from(primaryStage)
    // TODO: Use parameters to determine if we should start CSSFX, enable CSSFX logging, etc
    System.setProperty("cssfx.log", "true")
    System.setProperty("cssfx.log.level", "INFO")
    CSSFX.addConverter(ScalaSBTURIToPathConverter).start()
    // TODO: Collect PluginsMenuGUIPlugin instances and construct Plugins menu contents
    pluginsMenu.items = pluginsMenuGUIPlugins.map(_.getMenu)
    showDatabaseBackendPicker(false)

  def showDatabaseBackendPicker(backButtonVisible: Boolean): Unit =
    borderPane.center = DatabaseBackendPicker.getNode(backButtonVisible)

  def showMainView(): Unit =
    // TODO: We should probably make this cleaner...
    val mainView = new MainView
    borderPane.center = mainView.getNode