package com.oopman.collectioneer.db.migrations

import com.oopman.collectioneer.db.{Injection, traits}
import distage.Functoid
import izumi.fundamentals.platform.functional.Identity
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import javax.sql.DataSource


def executeMigrations(dataSource: DataSource): MigrateResult =
  // TODO: Determine migrations path based on DataSource type
  val f: Functoid[Identity[MigrateResult]] = (db: traits.DatabaseBackend) =>
    Flyway
      .configure()
      .dataSource(dataSource)
      .locations(
        db.migrationLocations: _*,
//        "classpath:migrations/h2", // TODO: Determine correct location based on database driver
//        "classpath:com/oopman/collectioneer/db/migrations/h2" // TODO: Determine correct location based on database driver
      )
      .load()
      .migrate()
  Injection.produceRun(dataSource, false)(f)

