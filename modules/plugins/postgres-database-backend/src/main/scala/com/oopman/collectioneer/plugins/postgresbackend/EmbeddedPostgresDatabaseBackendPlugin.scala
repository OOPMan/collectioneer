package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.Config
import de.softwareforge.testing.postgres.embedded.EmbeddedPostgres
import distage.Id
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
class EmbeddedPostgresDatabaseBackendPlugin
(override val config: Config, val removeDataOnShutdown: Boolean @Id("com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin.removeDataOnShutdown") = false )
extends PostgresDatabaseBackendPlugin(config):
  override def getName = "Embedded PostgreSQL Database Backend"

  override def getShortName = "embeddedpostgresbackend"

  override def getVersion = "master"

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
        .embeddedPostgresMap
        .getOrElseUpdate(datasourceUri, getEmbeddedPostgres)
      if (database != EmbeddedPostgresDatabaseBackendPlugin.defaultDatabase) then
        val dataSource = embeddedPostgres.createDefaultDataSource()
        val connection = dataSource.getConnection
        val preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM pg_database WHERE datname = ?")
        preparedStatement.setString(1, database)
        val resultSet = preparedStatement.executeQuery()
        if !resultSet.next() then throw new RuntimeException(s"Empty ResultSet while trying to determine if database $database exists!")
        val databaseExists = resultSet.getInt(1) == 1
        if !databaseExists then connection.createStatement().execute(s"CREATE DATABASE $database")
      embeddedPostgres.createDataSource("postgres", database)
    case _ => throw RuntimeException("config.datasourceUri undefined!")
  }

  override def getConnection: Connection =
    getDatasource.getConnection

  override def shutDown(): Unit =
    super.shutDown()
    for
      datasourceUri <- config.datasourceUri
      embeddedPostgres <- EmbeddedPostgresDatabaseBackendPlugin.embeddedPostgresMap.get(datasourceUri)
    do
      embeddedPostgres.close()
      EmbeddedPostgresDatabaseBackendPlugin.embeddedPostgresMap.remove(datasourceUri)


object EmbeddedPostgresDatabaseBackendPlugin:
  private val embeddedPostgresMap: collection.mutable.Map[String, EmbeddedPostgres] = collection.mutable.Map()

  protected val prefix: String = "jdbc:embeddedpostgresql:"
  private val defaultDatabase = "postgres"
  private val defaultPath = (os.home / "collectioneer" / "default").toString
  protected val codec: PercentCodec = PercentCodec(":/?#[]@!$&'()*+,;=".getBytes, false) // See https://datatracker.ietf.org/doc/html/rfc3986#section-2.2

  def encodePercentString(string: String): String =
    codec.encode(string.getBytes).map(_.toChar).mkString

  def decodePercentString(string: String): String =
    codec.decode(string.getBytes).map(_.toChar).mkString

  def parseUrl(url: String): (String, String, Map[String, String]) =
    if !url.startsWith(prefix) then throw RuntimeException("Invalud JDBC URL!")
    val (server, parameters) = url.split("\\?", 2) match {
      case Array(server, parameters) => (server, parameters)
      case Array(server) => (server, "")
    }
    val (path, database) = server.split('/') match {
      case Array(_, "", path, database) => (Some(path), database)
      case Array(_, "", path) => (Some(path), defaultDatabase)
      case Array(_) => (None, defaultDatabase)
      case _ => throw RuntimeException("Invalid JDBC URL!")
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
