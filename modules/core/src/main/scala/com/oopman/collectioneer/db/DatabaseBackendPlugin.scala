package com.oopman.collectioneer.db

import com.oopman.collectioneer.Plugin
import izumi.distage.model.definition.ModuleDef

trait DatabaseBackendPlugin extends Plugin:
  def startUp(): Boolean
  def shutDown(): Boolean
  def compatibleWithDatasourceUri: Boolean
  def getDatabaseBackendModule: ModuleDef
  