package com.oopman.collectioneer.gui

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.plugins.DatabaseBackendGUIPlugin
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ChoiceBox, Menu, MenuBar, MenuItem, ProgressIndicator}
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.scene.paint.*
import scalafx.scene.paint.Color.*
import scalafx.scene.text.Text
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.stage.Stage
import distage.ModuleDef
import scalafx.concurrent.Task

import java.util.concurrent.ExecutorService
import scala.language.implicitConversions
import scala.util.Try

object CollectioneerGUI extends JFXApp3 :
  // TODO: Create lazy vals for menu items
  // TODO: Implement handlers for menu items
  // TODO: Tie menu-bar width to stage width somehow

  lazy val borderPane = new BorderPane:
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

  lazy val collectioneerGUIModule = new ModuleDef:
    make[Stage].named("com.oopman.collectioneer.plugins.GUIPlugin.stage").from(primaryStage)

  override def start(): Unit =
    stage = primaryStage
    showDatabaseBackendPicker(false)

  def showDatabaseBackendPicker(backButtonVisible: Boolean): Unit =
    borderPane.center = DatabaseBackendPicker.getNode(backButtonVisible)

  def showMainView(config: GUIConfig): Unit =
    // TODO: We should probably make this cleaner...
    val mainView = new MainView(config)
    borderPane.center = mainView.getNode