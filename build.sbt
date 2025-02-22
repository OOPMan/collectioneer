import scala.collection.Seq

lazy val scala213Version      = "2.13.10"
lazy val scala3Version        = "3.6.3"
lazy val circeVersion         = "0.14.6"
lazy val circeLibraryDependencies = Seq(
  "io.circe" %% "circe-core"    % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser"  % circeVersion,
  "io.circe" %% "circe-yaml"    % "0.15.1",
  "io.circe" %% "circe-optics"  % "0.15.0"
)
lazy val sttpLibraryDependencies = Seq(
  "com.softwaremill.sttp.client3"   %% "core"  % "3.9.4",
  "com.softwaremill.sttp.client3"   %% "circe" % "3.9.4"
)
lazy val flywayVersion = "10.6.0"
lazy val scalikeJDBCDependencies = Seq(
  "org.flywaydb"                    % "flyway-core"                 % flywayVersion,
  "org.scalikejdbc"                 %% "scalikejdbc"                % "4.2.1",
  "org.scalikejdbc"                 %% "scalikejdbc-test"           % "4.2.1"               % "test",
)
lazy val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := scala3Version,
  crossScalaVersions := Seq(scala213Version, scala3Version),
  scalacOptions ++= Seq(
    "-Yretain-trees",
  ),
  libraryDependencies ++= Seq(
    "com.typesafe.scala-logging"      %% "scala-logging"              % "3.9.4",
    "ch.qos.logback"                  % "logback-classic"             % "1.5.6",
    "io.7mind.izumi"                  %% "distage-core"               % "1.2.16",
    "io.7mind.izumi"                  %% "distage-extension-plugins"  % "1.2.16",
    "com.novocode"                    % "junit-interface"             % "0.11"              % "test",
    "org.scalactic"                   %% "scalactic"                  % "3.2.18",
    "org.scalatest"                   %% "scalatest"                  % "3.2.18"            % "test",
    "org.typelevel"                   %% "shapeless3-deriving"        % "3.4.3",
    "org.typelevel"                   %% "shapeless3-typeable"        % "3.4.3"
  ),
  libraryDependencies ++= circeLibraryDependencies,
)

lazy val collectioneer = project
  .in(file("."))
  .settings(commonSettings)
  .aggregate(
      core,
      cli,
      gui,
      plugins,
  )

lazy val core = project
  .in(file("modules/core"))
  .settings(commonSettings)
  .settings(
    name := "Collectioneer Core",
    libraryDependencies ++= Seq(
    )
  )

lazy val cliCore = project
  .in(file("modules/cli-core"))
  .settings(commonSettings)
  .settings(
    name := "Collectioneer CLI Core",
    libraryDependencies ++= Seq(
      "com.github.scopt" %% "scopt" % "4.1.0",
    ),
    scalacOptions ++= Seq(
      "-Xmax-inlines", "64"
    ),
  )
  .dependsOn(core)

lazy val cli = project
  .in(file("modules/cli"))
  .enablePlugins(PackPlugin)
  .settings(commonSettings)
  .settings(
    name := "Collectioneer CLI",
    fork := true,
    mainClass := Some("com.oopman.collectioneer.cli.CLI"),
    packMain := Map("collectioneer-cli" -> "com.oopman.collectioneer.cli.CLI"),
  )
  .dependsOn(
    core,
    cliCore,
    plugins,
  )

// Add JavaFX dependencies
lazy val gui = project
  .in(file("modules/gui"))
  .settings(commonSettings)
  .settings(
    name := "Collectioneer GUI",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature"),
    scalacOptions ++= (
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) =>
          Seq("-explain", "-explain-types", "-rewrite", "-source", "3.2-migration")
        case _ =>
          Seq("-Xlint", "-explaintypes")
      }
      ),
    fork := true,
    mainClass := Some("ScalaFXHelloWorld"),
    libraryDependencies ++= Seq(
      "org.scalafx"             %% "scalafx"          % "20.0.0-R31",
    )
  )
  .dependsOn(core)

lazy val scalikeJDBCDatabaseBackendCommon = project
  .in(file("modules/scalikejdbc-database-backend-common"))
  .settings(commonSettings)
  .settings(
    name := "ScalikeJDBC Database Backend Common",
    exportJars := true,
    libraryDependencies ++= scalikeJDBCDependencies
  )
  .dependsOn(core)

// Plugin projects
lazy val plugins = project
  .in(file("modules/plugins"))
  .settings(commonSettings)
  .aggregate(
    postgresDatabaseBackend,
    embeddedPostgresDatabaseBackendCLI,
    grandArchiveTCG,
  )
  .dependsOn(
    postgresDatabaseBackend,
    embeddedPostgresDatabaseBackendCLI,
    grandArchiveTCG
  )

lazy val postgresDatabaseBackend = project
  .in(file("modules/plugins/postgres-database-backend"))
  .settings(commonSettings)
  .settings(
    name := "PostgreSQL Database Backend",
    exportJars := true,
    libraryDependencies ++= scalikeJDBCDependencies,
    libraryDependencies ++= Seq(
      "de.softwareforge.testing"        % "pg-embedded"                 % "5.1.0",
      "org.postgresql"                  % "postgresql"                  % "42.7.3",
      "com.lihaoyi"                     %% "os-lib"                     % "0.9.3",
      "org.flywaydb"                    % "flyway-database-postgresql"  % flywayVersion,
    )
  )
  .dependsOn(core, scalikeJDBCDatabaseBackendCommon)

lazy val embeddedPostgresDatabaseBackendCLI = project
  .in(file("modules/plugins/embedded-postgres-database-backend-cli"))
  .settings(commonSettings)
  .settings(
    name := "Embedded PostgreSQL Database Backend CLI",
    exportJars := true,
    libraryDependencies ++= circeLibraryDependencies,
  )
  .dependsOn(cliCore, postgresDatabaseBackend)

lazy val grandArchiveTCG = project
  .in(file("modules/plugins/grand-archive-tcg"))
  .settings(commonSettings)
  .settings(
    name := "Grand Archive TCG Plugin",
    exportJars := true,
    libraryDependencies ++= Seq(
      "com.lihaoyi"                     %% "os-lib"                     % "0.9.3",
    ),
    libraryDependencies ++= sttpLibraryDependencies,
  )
  .dependsOn(core, cliCore)
