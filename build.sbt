import scala.collection.Seq

lazy val scala213Version      = "2.13.10"
lazy val scala3Version        = "3.3.1"
lazy val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := scala3Version,
  crossScalaVersions := Seq(scala213Version, scala3Version),
  scalacOptions ++= Seq(
    "-Yretain-trees",
  ),
  libraryDependencies ++= Seq(
    "ch.qos.logback"                  % "logback-classic"             % "1.2.3",
    "org.flywaydb"                    % "flyway-core"                 % "9.22.0",
    "org.scalikejdbc"                 %% "scalikejdbc"                % "4.0.0",
    "com.h2database"                  % "h2"                          % "2.2.222",
    "io.7mind.izumi"                  %% "distage-core"               % "1.1.0",
    "io.7mind.izumi"                  %% "distage-extension-plugins"  % "1.1.0",
    "com.softwaremill.sttp.client3"   %% "core"                       % "3.9.0",
    "com.novocode"                    % "junit-interface"             % "0.11"              % "test",
  ),
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
      "io.circe" %% "circe-core" % "0.14.2",
      "io.circe" %% "circe-generic" % "0.14.2",
      "io.circe" %% "circe-parser" % "0.14.2",
      "io.circe" %% "circe-yaml" % "0.14.2",
    ),
    scalacOptions ++= Seq(
      "-Xmax-inlines", "64"
    ),
  )

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

// Plugin projects
lazy val plugins = project
  .in(file("modules/plugins"))
  .settings(commonSettings)
  .aggregate(
    grandArchiveTCG,
  )
  .dependsOn(
    grandArchiveTCG
  )

lazy val grandArchiveTCG = project
  .in(file("modules/plugins/grand-archive-tcg"))
  .settings(commonSettings)
  .settings(
    name := "Grand Archive TCG Plugin",
    exportJars := true
  )
  .dependsOn(core, cliCore)
