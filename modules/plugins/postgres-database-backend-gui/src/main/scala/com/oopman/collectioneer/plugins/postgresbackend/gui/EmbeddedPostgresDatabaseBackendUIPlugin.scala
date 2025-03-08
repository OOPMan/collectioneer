package com.oopman.collectioneer.plugins.postgresbackend.gui

import com.oopman.collectioneer.plugins.{DatabaseBackendGUIPlugin, GUIPlugin}
import distage.Id
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.GridPane
import scalafx.stage.{DirectoryChooser, Stage}
import scalafx.Includes.*

class EmbeddedPostgresDatabaseBackendUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), DatabaseBackendGUIPlugin:
  protected lazy val directoryChooser = new DirectoryChooser:
    initialDirectory = os.home.toIO

  protected lazy val locationLabel = new Label:
    text = "Location"

  protected lazy val locationInput = new TextField

  protected lazy val locationChooserButton = new Button:
    text = "Pick"
    onAction = { e =>
      val path =  directoryChooser.showDialog(stage)
      locationInput.text = path.getAbsolutePath
    }

  protected lazy val databaseBaseLabel = new Label:
    text = "Database"

  protected lazy val databaseBaseNameInput = new TextField:
    text = "postgres"

  protected lazy val gridPane = new GridPane:
    hgap = 4
    vgap = 4
    add(locationLabel,          0, 0, 1, 1)
    add(locationInput,          1, 0, 3, 1)
    add(locationChooserButton,  4, 0, 1, 1)
    add(databaseBaseLabel,      0, 1, 1, 1)
    add(databaseBaseNameInput,  1, 1, 3, 1)

  def getName: String = "Embedded PostgreSQL Database Backend UI"

  def getShortName: String = "postgresbackendui"

  def getVersion: String = "master"

  def getUIName: String = "Embedded PostgreSQL Database Backend"

  def getNode: Node = gridPane

  def getDatasourceURI: String =
    // TODO: Generate URI
    ""
