package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.db.DatabaseBackendPlugin
import com.oopman.collectioneer.db.scalikejdbc.ScalikeJDBCDatabaseBackendPlugin
import com.oopman.collectioneer.plugins.postgresbacken.PostgresDatabaseBackendPlugin
import izumi.distage.plugins.PluginDef

object PostgresDatabaseBackendPluginDef extends PluginDef:
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
