package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.Config
import de.softwareforge.testing.postgres.embedded.EmbeddedPostgres

import java.util.UUID
import javax.sql.DataSource

class TestPostgresDatabaseBackendPlugin(override val config: Config) extends EmbeddedPostgresDatabaseBackendPlugin(config, removeDataOnShutdown = true):
  protected lazy val embeddedPostgres = EmbeddedPostgres
    .builderWithDefaults()
    .setDataDirectory((os.pwd / UUID.randomUUID().toString).wrapped)
    .setRemoveDataOnShutdown(true)
    .setServerVersion("15")
    .build()

  override def compatibleWithDatasourceUri: Boolean =
    config.datasourceUri.contains("jdbc:testpostgresql:")

  override def getDatasource: DataSource =
    embeddedPostgres.createDefaultDataSource()

  override def shutDown(): Unit = embeddedPostgres.close()
