package com.oopman.collectioneer.plugins.postgresbackend.gui

import com.oopman.collectioneer.plugins.DatabaseBackendGUIPlugin
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.GridPane

class EmbeddedPostgresDatabaseBackendUIPlugin extends DatabaseBackendGUIPlugin:
  protected lazy val locationLabel = new Label:
    text = "Location"

  protected lazy val locationInput = new TextField

  protected lazy val locationChooserButton = new Button:
    text = "Pick"

  protected lazy val databaseBaseLabel = new Label:
    text = "Database"

  protected lazy val databaseBaseNameInput = new TextField:
    text = "postgres"

  GridPane.setConstraints(locationLabel, 0, 0, 1, 1)
  GridPane.setConstraints(locationInput, 1, 0, 3, 1)
  GridPane.setConstraints(locationChooserButton, 4, 0, 1, 1)
  GridPane.setConstraints(databaseBaseLabel, 0, 1, 1, 1)
  GridPane.setConstraints(databaseBaseNameInput, 1, 1, 3, 1)
  protected lazy val gridPane = new GridPane:
    hgap = 4
    vgap = 4
    children ++= Seq(locationLabel, locationInput, locationChooserButton, databaseBaseLabel, databaseBaseNameInput)

  def getName: String = "Embedded PostgreSQL Database Backend UI"

  def getShortName: String = "postgresbackendui"

  def getVersion: String = "master"

  def getUIName: String = "Embedded PostgreSQL Database Backend"

  def getNode: Node = gridPane

  def getDatasourceURI: String =
    // TODO: Generate URI
    ""
