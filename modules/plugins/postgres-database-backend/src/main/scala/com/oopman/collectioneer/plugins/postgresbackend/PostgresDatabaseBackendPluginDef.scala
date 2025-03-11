package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.db.scalikejdbc.ScalikeJDBCDatabaseBackendPlugin
import izumi.distage.plugins.PluginDef

object PostgresDatabaseBackendPluginDef extends PluginDef:
  make[Boolean].named("com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin.removeDataOnShutdown").from(false)
  many[DatabaseBackendPlugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
    .add[TestPostgresDatabaseBackendPlugin]
  many[ScalikeJDBCDatabaseBackendPlugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
    .add[TestPostgresDatabaseBackendPlugin]
  many[Plugin]
    .add[PostgresDatabaseBackendPlugin]
    .add[EmbeddedPostgresDatabaseBackendPlugin]
    .add[TestPostgresDatabaseBackendPlugin]
