package com.oopman.collectioneer.db.migrations

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.db.{Injection, traits}
import distage.Functoid
import izumi.fundamentals.platform.functional.Identity
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import javax.sql.DataSource


def executeMigrations(dataSource: DataSource): Set[MigrateResult] =
  def executeMigrations(db: traits.DatabaseBackend, plugins: Set[Plugin]) =
    val migrationResult = Flyway
      .configure()
      .table("collectioneer_migration_history")
      .dataSource(dataSource)
      .locations(db.migrationLocations: _*)
      .load()
      .migrate()
    val pluginMigrationResults = plugins
      .map( plugin => (plugin.getShortName, plugin.getMigrationLocations(db)))
      .filter(_._2.nonEmpty)
      .map { (shortName: String, migrationLocations: Seq[String]) =>
        Flyway
          .configure()
          .baselineOnMigrate(true)
          .baselineVersion("0")
          .table(s"${shortName}_plugin_migration_history")
          .dataSource(dataSource)
          .locations(migrationLocations: _*)
          .load()
          .migrate()
      }
    pluginMigrationResults + migrationResult
  Injection.produceRun(dataSource, false)(executeMigrations)

