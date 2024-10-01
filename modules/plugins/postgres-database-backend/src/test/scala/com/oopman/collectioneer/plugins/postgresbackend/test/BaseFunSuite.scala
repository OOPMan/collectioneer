package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.db.scalikejdbc.ScalikeJDBCDatabaseBackendPlugin
import com.oopman.collectioneer.db.{DatabaseBackendPlugin, Injection}
import com.oopman.collectioneer.plugins.postgresbacken.TestPostgresDatabaseBackendPlugin
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.FixtureAnyFlatSpec
import scalikejdbc.*
import scalikejdbc.scalatest.AutoRollback

object T:
  protected object Config extends com.oopman.collectioneer.Config:
    override val datasourceUri: String = "jdbc:testpostgresql"

  lazy val initialized: Boolean = Injection.produceRun(Config) { (databaseBackendPlugin: DatabaseBackendPlugin) =>
    databaseBackendPlugin.startUp()
    true
  }

abstract class BaseFunSuite extends FixtureAnyFlatSpec with AutoRollback with BeforeAndAfterAll:

  override def db(): DB =
    if (!T.initialized) throw RuntimeException("Boo!")
    DB(conn = ConnectionPool.borrow(), settingsProvider = SettingsProvider.default)
