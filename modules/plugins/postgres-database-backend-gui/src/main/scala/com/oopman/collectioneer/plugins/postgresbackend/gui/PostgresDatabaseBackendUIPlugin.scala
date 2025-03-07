package com.oopman.collectioneer.plugins.postgresbackend.gui

import com.oopman.collectioneer.plugins.DatabaseBackendGUIPlugin
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.GridPane


class PostgresDatabaseBackendUIPlugin extends DatabaseBackendGUIPlugin:
  protected lazy val hostLabel = new Label:
    text = "Host"

  protected lazy val hostTextField = new TextField

  protected lazy val portLabel = new Label:
    text = "Port"

  protected lazy val portTextField = new TextField

  protected lazy val databaseLabel = new Label:
    text = "Database"

  protected lazy val databaseTextField = new TextField:
    text = "postgres"

  protected lazy val gridPane = new GridPane:
    hgap = 4
    vgap = 4
    add(hostLabel,          0, 0, 1, 1)
    add(hostTextField,      1, 0, 3, 1)
    add(portLabel,          0, 1, 1, 1)
    add(portTextField,      1, 1, 3, 1)
    add(databaseLabel,      0, 2, 1, 1)
    add(databaseTextField,  1, 2, 3, 1)

  def getName: String = "PostgreSQL Database Backend UI"

  def getShortName: String = "postgresbackendui"

  def getVersion: String = "master"

  def getUIName: String = "PostgreSQL Database Backend"

  def getNode: Node = gridPane

  def getDatasourceURI: String =
    // TODO: Generate URI
    ""







