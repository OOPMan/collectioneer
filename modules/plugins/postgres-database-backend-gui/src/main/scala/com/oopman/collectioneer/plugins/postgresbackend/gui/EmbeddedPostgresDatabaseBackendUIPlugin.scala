package com.oopman.collectioneer.plugins.postgresbackend.gui

import com.oopman.collectioneer.plugins.{DatabaseBackendGUIPlugin, GUIPlugin}
import com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin.encodePercentString
import distage.Id
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.GridPane
import scalafx.stage.{DirectoryChooser, Stage}
import scalafx.Includes.*

class EmbeddedPostgresDatabaseBackendUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), DatabaseBackendGUIPlugin:
  // TODO: Normalize variable names

  private lazy val directoryChooser = new DirectoryChooser:
    initialDirectory = os.home.toIO

  private lazy val locationLabel = new Label:
    text = "Location"

  private lazy val locationTextField = new TextField

  private lazy val locationPickerButton = new Button:
    text = "Pick"
    onAction = { e =>
      Option(directoryChooser.showDialog(stage)).map(path => locationTextField.text = path.getAbsolutePath)
    }

  private lazy val databaseBaseLabel = new Label:
    text = "Database"

  private lazy val databasebaseTextField = new TextField:
    text = "postgres"

  private lazy val gridPane = new GridPane:
    hgap = 4
    vgap = 4
    add(locationLabel,          0, 0, 1, 1)
    add(locationTextField,      1, 0, 3, 1)
    add(locationPickerButton,   4, 0, 1, 1)
    add(databaseBaseLabel,      0, 1, 1, 1)
    add(databasebaseTextField,  1, 1, 3, 1)

  def getName: String = "Embedded PostgreSQL Database Backend UI"

  def getShortName: String = "postgresbackendui"

  def getVersion: String = "master"

  def getUIName: String = "Embedded PostgreSQL Database Backend"

  def getNode: Node = gridPane

  def getDatasourceURI: String =
    val location = encodePercentString(locationTextField.getText)
    val database = encodePercentString(databasebaseTextField.getText)
    s"jdbc:embeddedpostgresql://$location/$database"
