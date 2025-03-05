package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.Config
import com.oopman.collectioneer.plugins.postgresbacken.PostgresDatabaseBackendPlugin
import de.softwareforge.testing.postgres.embedded.EmbeddedPostgres
import org.apache.commons.codec.net.PercentCodec

import java.sql.Connection
import javax.sql.DataSource
import scala.util.Try

/**
 * EmbeddedPostgresDatabaseBackendPlugin JDBC URI format:
 *
 * jdbc:embeddedpostgresql://PATH/DATABASE?NAME_A=VALUE_A&NAME_B=VALUE_B
 *
 * PATH, DATABASE, NAME_ and VALUE_ elements must be percent encoded as per RFC 3986
 *
 * If PATH is omitted it defaults to /path/to/<USER_DATA_DIR>/collection
 * If DATABASE is omitted it defaults to postgres
 * If DATABASE is set and is NOT postgres then prior to handing out a DataSource/Connection the Default DataSource will
 * be used to CREATE DATABASE
 *
 * @param config
 */
class EmbeddedPostgresDatabaseBackendPlugin(override val config: Config, removeDataOnShutdown: Boolean = false)
extends PostgresDatabaseBackendPlugin(config):
  // TODO: Maybe we should override the getName methods?
  override def compatibleWithDatasourceUri: Boolean =
    config.datasourceUri.flatMap(datasourceUri => Try(EmbeddedPostgresDatabaseBackendPlugin.parseUrl(datasourceUri)).toOption).isDefined

  override def getDatasource: DataSource = config.datasourceUri match {
    case Some(datasourceUri) =>
      val (path, database, parameters) = EmbeddedPostgresDatabaseBackendPlugin.parseUrl(datasourceUri)
      def getEmbeddedPostgres =
        var builder = EmbeddedPostgres
          .builderWithDefaults()
          .setDataDirectory(path)
          .setRemoveDataOnShutdown(removeDataOnShutdown)
          .setServerVersion("15")
        for((key, value) <- parameters) builder = builder.addConnectionProperty(key, value)
        builder.build()
      val embeddedPostgres = EmbeddedPostgresDatabaseBackendPlugin
        .configEmbeddedPostgresMap
        .getOrElseUpdate(datasourceUri, getEmbeddedPostgres)
      if (database != EmbeddedPostgresDatabaseBackendPlugin.defaultDatabase) then
        val dataSource = embeddedPostgres.createDefaultDataSource()
        val connection = dataSource.getConnection
        val statement = connection.createStatement()
        statement.execute(s"CREATE DATABASE $database")
      embeddedPostgres.createDataSource("postgres", database)
    case _ => throw RuntimeException("config.datasourceUri undefined!")
  }

  override def getConnection: Connection =
    getDatasource.getConnection

  override def shutDown(): Unit =
    super.shutDown()
    for {
      datasourceUri <- config.datasourceUri
      embeddedPostgres <- EmbeddedPostgresDatabaseBackendPlugin.configEmbeddedPostgresMap.get(datasourceUri)
    } embeddedPostgres.close()

object EmbeddedPostgresDatabaseBackendPlugin:
  protected val configEmbeddedPostgresMap: collection.mutable.Map[String, EmbeddedPostgres] = collection.mutable.Map()

  protected val prefix = "jdbc:embeddedpostgresql:"
  protected val defaultDatabase = "postgres"
  protected val defaultPath = (os.home / "collectioneer" / "default").toString
  protected val codec = PercentCodec(":/?#[]@!$&'()*+,;=".getBytes, false) // See https://datatracker.ietf.org/doc/html/rfc3986#section-2.2

  protected def decodePercentString(string: String): String =
    codec.decode(string.getBytes).map(_.toChar).mkString

  def parseUrl(url: String) =
    val (server, parameters) = url.split("\\?", 2) match {
      case Array(server, parameters) => (server, parameters)
      case Array(server) => (server, "")
    }
    val (path, database) = server.split('/') match {
      case Array(prefix, "", path, database) => (Some(path), database)
      case Array(prefix, "", path) => (Some(path), defaultDatabase)
      case Array(prefix) => (None, defaultDatabase)
      case _ => throw RuntimeException("Invalid URI!")
    }
    val decodedPath = path.map(decodePercentString).getOrElse(defaultPath)
    val decodedDatabase = decodePercentString(database)
    val parametersMap = parameters
      .split('&')
      .map(_.split("=", 2))
      .flatMap {
        case Array(key, value) => Some((decodePercentString(key), decodePercentString(value)))
        case _ => None
      }
      .toMap
    (decodedPath, decodedDatabase, parametersMap)
