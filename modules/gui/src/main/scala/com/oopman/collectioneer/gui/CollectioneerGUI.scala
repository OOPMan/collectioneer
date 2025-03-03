package com.oopman.collectioneer.gui

import com.oopman.collectioneer.db.Injection
import com.oopman.collectioneer.plugins.DatabaseBackendGUIPlugin
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{ChoiceBox, Menu, MenuBar, MenuItem}
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.scene.paint.*
import scalafx.scene.paint.Color.*
import scalafx.scene.text.Text
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer

import scala.language.implicitConversions

object CollectioneerGUI extends JFXApp3 :
  // TODO: Create lazy vals for menu items
  // TODO: Implement handlers for menu items
  // TODO: Tie menu-bar width to stage width somehow

  override def start(): Unit =

    stage = new JFXApp3.PrimaryStage :
      title = "Collectioneer"
      width = 1024
      height = 768
      scene = new Scene :
        content = new BorderPane:
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
          center = DatabaseBackendPicker.getNode