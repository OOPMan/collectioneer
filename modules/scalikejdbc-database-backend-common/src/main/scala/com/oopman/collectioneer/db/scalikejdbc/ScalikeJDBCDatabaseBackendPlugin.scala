package com.oopman.collectioneer.db.scalikejdbc

import com.oopman.collectioneer.db.DatabaseBackendPlugin

import java.sql.Connection
import javax.sql.DataSource

trait ScalikeJDBCDatabaseBackendPlugin extends DatabaseBackendPlugin:
  def getDatasource: DataSource
  def getConnection: Connection
  def getMigrationLocations: Seq[String]

