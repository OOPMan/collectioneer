package com.oopman.collectioneer.db

import com.oopman.collectioneer.{Config, Plugin}
import izumi.distage.model.definition.ModuleDef

trait DatabaseBackendPlugin extends Plugin:
  val config: Config
  // TODO: Return Try for startUp and shutDown
  def startUp(): Unit
  def shutDown(): Unit
  def compatibleWithDatasourceUri: Boolean
  def getDatabaseBackendModule: ModuleDef
  