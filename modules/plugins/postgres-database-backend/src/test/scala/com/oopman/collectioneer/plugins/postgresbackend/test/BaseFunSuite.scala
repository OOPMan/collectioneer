package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.FixtureAnyFlatSpec
import scalikejdbc.*
import scalikejdbc.scalatest.AutoRollback

object DatabaseBackendPluginSupport:
  protected object Config extends com.oopman.collectioneer.Config:
    val datasourceUri: Option[String] = Some("jdbc:testpostgresql:")
    val subConfigs: Map[String, com.oopman.collectioneer.SubConfig] = Map.empty

  lazy val initialized: Boolean = Injection.produceRun(Some(Config)) { (databaseBackendPlugin: DatabaseBackendPlugin) =>
    databaseBackendPlugin.startUp()
    true
  }

abstract class BaseFunSuite extends FixtureAnyFlatSpec with AutoRollback with BeforeAndAfterAll:

  override def db(): DB =
    if (!DatabaseBackendPluginSupport.initialized) throw RuntimeException("Failed to initialize DatabaseBackendPlugin")
    DB(conn = ConnectionPool.borrow(), settingsProvider = SettingsProvider.default)

class DatabaseBackendPluginSupportInit extends BaseFunSuite:

  behavior of "DatabaseBackendPluginSupportInit"

  it should "Initialize the Database" in { implicit session => assert(true) }
