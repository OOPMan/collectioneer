package com.oopman.collectioneer.plugins.postgresbackend.gui

import com.oopman.collectioneer.plugins.{DatabaseBackendGUIPlugin, GUIPlugin}
import com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin.encodePercentString
import distage.Id
import javafx.util.converter.IntegerStringConverter
import scalafx.scene.Node
import scalafx.scene.control.{Label, PasswordField, TextField, TextFormatter}
import scalafx.scene.layout.GridPane
import scalafx.stage.Stage
import scalafx.Includes.*

class PostgresDatabaseBackendUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), DatabaseBackendGUIPlugin:
  private lazy val hostLabel = new Label:
    text = "Host"

  private lazy val hostTextField = new TextField

  private lazy val portLabel = new Label:
    text = "Port"

  private lazy val portTextField = new TextField:
    textFormatter = new TextFormatter(new IntegerStringConverter)

  private lazy val databaseLabel = new Label:
    text = "Database"

  private lazy val databaseTextField = new TextField:
    text = "postgres"

  private lazy val userLabel = new Label:
    text = "User"

  private lazy val userTextField = new TextField:
    text = "postgres"

  private lazy val passwordLabel = new Label:
    text = "Password"
  private lazy val passwordField = new PasswordField

  private lazy val gridPane = new GridPane:
    hgap = 4
    vgap = 4
    add(hostLabel,          0, 0, 1, 1)
    add(hostTextField,      1, 0, 3, 1)
    add(portLabel,          0, 1, 1, 1)
    add(portTextField,      1, 1, 3, 1)
    add(databaseLabel,      0, 2, 1, 1)
    add(databaseTextField,  1, 2, 3, 1)
    add(userLabel,          0, 3, 1, 1)
    add(userTextField,      1, 3, 3, 1)
    add(passwordLabel,      0, 4, 1, 1)
    add(passwordField,      1, 4, 3, 1)

  def getName: String = "PostgreSQL Database Backend UI"

  def getShortName: String = "postgresbackendui"

  def getVersion: String = "master"

  def getUIName: String = "PostgreSQL Database Backend"

  def getNode: Node = gridPane

  def getDatasourceURI: String =
    val host = encodePercentString(hostTextField.getText)
    val port = encodePercentString(portTextField.getText)
    val database = encodePercentString(databaseTextField.getText)
    val user = encodePercentString(userTextField.getText)
    val password = encodePercentString(passwordField.getText)
    s"jdbc:postgresql://$host:$port/$database?user=$user&password=$password"







