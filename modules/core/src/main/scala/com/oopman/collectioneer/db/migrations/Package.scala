package com.oopman.collectioneer.db.migrations

import com.oopman.collectioneer.db.{Injection, traits}
import distage.Functoid
import izumi.fundamentals.platform.functional.Identity
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import javax.sql.DataSource


def executeMigrations(dataSource: DataSource): MigrateResult =
  def executeMigrations(db: traits.DatabaseBackend) =
    Flyway
      .configure()
      .dataSource(dataSource)
      .locations(db.migrationLocations: _*)
      .load()
      .migrate()
  Injection.produceRun(dataSource, false)(executeMigrations)

