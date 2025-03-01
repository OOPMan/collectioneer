package com.oopman.collectioneer.plugins.postgresbackend.gui

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.plugins.{DatabaseBackendGUIPlugin, GUIPlugin}
import izumi.distage.plugins.PluginDef
import scalafx.scene.Node


class PostgresDatabaseBackendUIPlugin extends DatabaseBackendGUIPlugin:
  def getName: String = "PostgreSQL Database Backend UI"

  def getShortName: String = "postgresbackendui"

  def getVersion: String = "master"

  def getUIName: String = "PostgreSQL Database Backend"

  def getNode: Node = ???

  def getDatabaseBackendPlugin: DatabaseBackendPlugin = ???


class EmbeddedPostgresDatabaseBackendUIPlugin extends DatabaseBackendGUIPlugin:
  def getName: String = "Embedded PostgreSQL Database Backend UI"

  def getShortName: String = "postgresbackendui"

  def getVersion: String = "master"

  def getUIName: String = "PostgreSQL Database Backend"

  def getNode: Node = ???

  def getDatabaseBackendPlugin: DatabaseBackendPlugin = ???


object PostgresDatabaseBackendUIPluginDef extends PluginDef:
  many[DatabaseBackendGUIPlugin]
    .add[PostgresDatabaseBackendUIPlugin]
    .add[EmbeddedPostgresDatabaseBackendUIPlugin]
  many[GUIPlugin]
    .add[PostgresDatabaseBackendUIPlugin]
    .add[EmbeddedPostgresDatabaseBackendUIPlugin]
  many[Plugin]
    .add[PostgresDatabaseBackendUIPlugin]
    .add[EmbeddedPostgresDatabaseBackendUIPlugin]

