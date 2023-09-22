package com.oopman.collectioneer.db.migrations

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import javax.sql.DataSource


def executeMigrations(dataSource: DataSource): MigrateResult =
  // TODO: Determine migrations path based on DataSource type
  Flyway
    .configure()
    .dataSource(dataSource)
    .locations(
      "classpath:migrations/h2", // TODO: Determine correct location based on database driver
      "classpath:com/oopman/collectioneer/db/migrations/h2" // TODO: Determine correct location based on database driver
    )
    .load()
    .migrate()

