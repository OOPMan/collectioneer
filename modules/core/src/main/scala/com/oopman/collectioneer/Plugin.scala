package com.oopman.collectioneer

trait Plugin:
  def getName: String
  def getShortName: String
  def getVersion: String
  def getMigrationLocations(databaseBackend: db.traits.DatabaseBackend): Seq[String]
