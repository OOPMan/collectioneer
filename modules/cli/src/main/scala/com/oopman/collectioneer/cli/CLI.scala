package com.oopman.collectioneer.cli

import org.flywaydb.core.Flyway
import scopt.OParser

import java.io.File

object CLI {
  val builder = OParser.builder[Config]
  val parser = {
    import builder._
    OParser.sequence(
      programName("Collectioneer CLI"),
      head("collectioneer-cli", "master"),
      help("help").text("Coming soon..."),
      opt[Boolean]("verbose")
        .action((_, config) => config.copy(verbose = true))
        .text("Enable verbose output"),
      opt[Boolean]("debug")
        .action((_, config) => config.copy(debug = true))
        .text("Enable debugging output"),
      opt[File]("collection")
        .action((file, config) => config.copy(collection = file)),
      cmd("init")
        .action((_, config) => config.copy(command = "initDB"))
        .text("Initialize the Collection DB")
    )
  }
  def main(args: Array[String]): Unit = {
    OParser.parse(parser, args, Config()) match {
      case Some(config) =>
        config.command match {
          case "initDB" =>
            // TODO: Implement DB init
            Flyway
              .configure()
              .dataSource(s"jdbc:h2:${config.collection.getAbsolutePath}", "sa", "")
              .locations(
                "classpath:migrations/h2",
                "classpath:com/oopman/collectioneer/db/migrations/h2"
              )
              .load()
              .migrate()
        }
      case _ =>
    }


  }
}
