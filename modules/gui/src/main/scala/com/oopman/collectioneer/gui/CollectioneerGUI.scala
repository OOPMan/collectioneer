package com.oopman.collectioneer.gui

import com.oopman.collectioneer.db.{DatabaseBackendPlugin, Injection}
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
  lazy val okayButton: Button = new Button:
    text = "Okay"
    onAction = { e =>
      // TODO: Handle config.datasourceUri being None
      okayButton.disable = true
      progressIndicator.visible = true
      val config = DatabaseBackendPicker.getConfig()
      val worker = Task {
        Injection.produceRun(Some(config)) {
          (databaseBackendPlugin: DatabaseBackendPlugin) => databaseBackendPlugin.startUp()
        }
      }
      worker.onSucceeded = { e =>
        progressIndicator.visible = false
      }
      worker.onFailed = { e =>
        progressIndicator.visible = false
        okayButton.disable = false
      }
      val thread = new Thread(worker)
      thread.setDaemon(true)
      thread.start()
    }

  lazy val progressIndicator = new ProgressIndicator:
    visible = false

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
    bottom = new HBox:
      children = Seq(okayButton, progressIndicator)
    
  lazy val primaryStage = new JFXApp3.PrimaryStage:
    title = "Collectioneer"
    width = 1024
    height = 768
    scene = new Scene :
      content = borderPane

  lazy val collectioneerGUIModule = new ModuleDef:
    make[Stage].named("com.oopman.collectioneer.plugins.GUIPlugin.stage").from(primaryStage)

  override def start(): Unit =
    stage = primaryStage
    borderPane.center = DatabaseBackendPicker.getNode