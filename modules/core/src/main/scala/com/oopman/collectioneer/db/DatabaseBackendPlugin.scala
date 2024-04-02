package com.oopman.collectioneer.db

import com.oopman.collectioneer.Plugin
import izumi.distage.model.definition.ModuleDef

import java.sql.Connection
import javax.sql.DataSource

trait DatabaseBackendPlugin extends Plugin:
  def compatibleWithDatasourceUri: Boolean
  def getDatasource: DataSource
  def getConnection: Connection
  def getMigrationLocations: Seq[String]
  def getDatabaseBackendModule: ModuleDef
  