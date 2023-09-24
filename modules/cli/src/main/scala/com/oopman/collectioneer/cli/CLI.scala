package com.oopman.collectioneer.cli

import com.oopman.collectioneer.db.migrations.executeMigrations
import com.oopman.collectioneer.db.dao.CollectionsDAO
import com.oopman.collectioneer.db.entity.Collections
import org.flywaydb.core.Flyway
import org.h2.jdbcx.JdbcDataSource
import scopt.OParser
import scalikejdbc._


import java.io.File
import java.util.UUID

import io.circe.generic.auto._
import io.circe.syntax._
import cats.syntax.either._
import io.circe.yaml._
import io.circe.yaml.syntax._

object CLI:
  val builder = OParser.builder[Config]
  val parser =
    import builder._
    val uuidArgs = arg[String]("<UUID>...")
      .unbounded()
      .required()
      // TODO: Validate UUIDs
      .text("One or more UUIDs")
    val deletedOpt = opt[Boolean]("deleted")
      .optional()
      .action((deleted, config) => config) // TODO: Set options on something
      .text("TODO:")
    val virtualOp = opt[Boolean]("virtual")
      .optional()
      .action((virtual, config) => config) // TODO: Set options on something
      .text("TODO:")
    OParser.sequence(
      programName("Collectioneer CLI"),
      head("collectioneer-cli", "master"),
      help("help").text("Coming soon..."),
      opt[Unit]("json")
        .action((_, config) => config.copy(outputFormat = OutputFormat.json))
        .text("Enable JSON output"),
      opt[Unit]("verbose")
        .action((_, config) => config.copy(verbose = true))
        .text("Enable verbose output"),
      opt[Unit]("debug")
        .action((_, config) => config.copy(debug = true))
        .text("Enable debugging output"),
      opt[String]("datasourceUri")
        .text("JDBC URI")
        // TODO: Add validator
        .action((datasourceUri, config) => config.copy(datasourceUri = datasourceUri)),
      opt[String]("datasourceUsername")
        .text("Datasource username")
        .action((datasourceUsername, config) => config.copy(datasourceUsername = datasourceUsername)),
      opt[String]("datasourcePassword")
        .text("Datasource password")
        .action((datasourcePassword, config) => config.copy(datasourcePassword = datasourcePassword)),
      cmd(Verbs.list.toString)
        .text("List Collections or Properties")
        .action((_, config) => config.copy(verb = Some(Verbs.list)))
        .children(
          cmd(Subjects.collections.toString)
            .text("List All Collections")
            .action((_, config) => config.copy(subject = Some(Subjects.collections)))
            .children(
              deletedOpt,
              virtualOp
              // TODO: Add filtering options
            ),
          cmd(Subjects.properties.toString)
            .text("List All Properties")
            .action((_, config) => config.copy(subject = Some(Subjects.collections)))
            .children(
              deletedOpt
              // TODO: Add filtering options
            ),
        ),
      cmd(Verbs.get.toString)
        .text("Get Collections or Properties")
        .action((_, config) => config.copy(verb = Some(Verbs.get)))
        .children(
          cmd(Subjects.collections.toString)
            .text("Get 1..n Collections")
            .action((_, config) => config.copy(subject = Some(Subjects.collections)))
            .children(
              uuidArgs
              // TODO: Add args option for UUIDs
            ),
          cmd(Subjects.properties.toString)
            .text("Get 1..n Properties")
            .action((_, config) => config.copy(subject = Some(Subjects.properties)))
            .children(
              uuidArgs
              // TODO: Add args option for UUIDs
            ),
        )
    )
  def main(args: Array[String]): Unit =
    OParser.parse(parser, args, Config()) match
      case Some(config) =>
        // Setup DataSoure
        val dataSource = new JdbcDataSource();
        dataSource.setURL(config.datasourceUri)
        dataSource.setUser(config.datasourceUsername)
        dataSource.setPassword(config.datasourcePassword)
        // Execute Flyway Migrations
        executeMigrations(dataSource)
        // Configure ScalikeJDBC
        GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
          enabled = config.debug,
          singleLineMode = !config.verbose
        )
        ConnectionPool.singleton(new DataSourceConnectionPool(dataSource))
        ConnectionPool.add(config.datasourceUri, new DataSourceConnectionPool(dataSource))
        // Process CLI arguments
        val action = actions((config.verb, config.subject))
        val result = (config.outputFormat, action(config)) match {
          case (OutputFormat.json, r) => r.spaces2SortKeys
          case (OutputFormat.yaml, r) => r.asYaml.spaces2
        }
        println(result)

      case _ =>




