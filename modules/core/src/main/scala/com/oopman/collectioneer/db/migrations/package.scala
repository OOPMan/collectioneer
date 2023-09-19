package com.oopman.collectioneer.db.migrations

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

/**
 * TODO: Support PostgreSQL
 *
 * @param datasourceUri
 * @param datasourceUsername
 * @param datasourcePassword
 * @return
 */
def executeMigrations(datasourceUri: String, datasourceUsername: String = "sa", datasourcePassword: String = ""): MigrateResult =
  // TODO: Introspect database type from URI
  Flyway
    .configure()
    .dataSource(datasourceUri, datasourceUsername, datasourcePassword)
    .locations(
      "classpath:migrations/h2", // TODO: Determine correct location based on database driver
      "classpath:com/oopman/collectioneer/db/migrations/h2" // TODO: Determine correct location based on database driver
    )
    .load()
    .migrate()
