package com.oopman.collectioneer.gui

import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem}
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.paint.*
import scalafx.scene.paint.Color.*
import scalafx.scene.text.Text
import scalafx.Includes.*

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
        fill = Color.rgb(38, 38, 38)
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