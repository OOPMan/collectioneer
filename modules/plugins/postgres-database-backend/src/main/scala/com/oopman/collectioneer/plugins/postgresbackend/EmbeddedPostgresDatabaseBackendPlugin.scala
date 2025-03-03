package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.Config
import com.oopman.collectioneer.plugins.postgresbacken.PostgresDatabaseBackendPlugin
import de.softwareforge.testing.postgres.embedded.EmbeddedPostgres

import java.sql.Connection
import javax.sql.DataSource

class EmbeddedPostgresDatabaseBackendPlugin(override val config: Config) extends PostgresDatabaseBackendPlugin(config):
  // TODO: Maybe we should override the getName methods?

  override def compatibleWithDatasourceUri: Boolean =
    config.datasourceUri.exists(_.startsWith("jdbc:embeddedpostgresql:"))

  override def getDatasource: DataSource = config.datasourceUri match {
    case Some(datasourceUri) => EmbeddedPostgresDatabaseBackendPlugin.configEmbeddedPostgresMap.getOrElseUpdate(
      datasourceUri,
      EmbeddedPostgres
        .builderWithDefaults()
        // TODO: Customize based on URI
        .setDataDirectory((os.pwd / "collection").wrapped)
        .setRemoveDataOnShutdown(false)
        .setServerVersion("15")
        .build()
    ).createDefaultDataSource()
    case _ => throw RuntimeException("config.datasourceUri undefined!")
  }
  //    // TODO: Support non-default datasource
  //    configEmbeddedPostgresMap.getOrElseUpdate(
  //      config.datasourceUri,
  //      EmbeddedPostgres
  //        .builderWithDefaults()
  //        // TODO: Customize based on URI
  //        .setDataDirectory((os.pwd / "collection").wrapped)
  //        .setRemoveDataOnShutdown(false)
  //        .setServerVersion("15")
  //        .build()
  //    ).createDefaultDataSource()

  override def getConnection: Connection =
    // TODO: Support connectiuon with custom username/password
    getDatasource.getConnection

  override def shutDown(): Unit =
    super.shutDown()
    config.datasourceUri.foreach(EmbeddedPostgresDatabaseBackendPlugin.configEmbeddedPostgresMap.get(_).foreach(_.close()))

object EmbeddedPostgresDatabaseBackendPlugin:
  protected val configEmbeddedPostgresMap: collection.mutable.Map[String, EmbeddedPostgres] = collection.mutable.Map()