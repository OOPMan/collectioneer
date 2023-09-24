lazy val scala213Version      = "2.13.10"
lazy val scala3Version        = "3.2.2"
lazy val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := scala3Version,
  crossScalaVersions := Seq(scala213Version, scala3Version),
  libraryDependencies ++= Seq(
    "ch.qos.logback"          % "logback-classic"         % "1.2.3",
    "org.flywaydb"            % "flyway-core"             % "9.22.0",
    "org.scalikejdbc"         %% "scalikejdbc"            % "4.0.0",
    "com.h2database"          % "h2"                      % "2.2.222",
    "com.novocode"            % "junit-interface"         % "0.11"              % "test",
    "com.lihaoyi"             % "ammonite"                % "3.0.0-M0"          % "test"  cross CrossVersion.full
  ),
  Test / sourceGenerators += Def.task {
    val file = (sourceManaged in Test).value / "amm.scala"
    IO.write(file, """object amm extends App { ammonite.AmmoniteMain.main(args) }""")
    Seq(file)
  }.taskValue,
  Test / fullClasspath ++= {
    (Test / updateClassifiers).value
      .configurations
      .find(_.configuration.name == Test.name)
      .get
      .modules
      .flatMap(_.artifacts)
      .collect{case (a, f) if a.classifier == Some("sources") => f}
  }
)

lazy val collectioneer = project
  .in(file("."))
  .settings(commonSettings)
  .aggregate(
      core,
      cli,
      gui,
      repl
  )

lazy val core = project
  .in(file("modules/core"))
  .settings(commonSettings)
  .settings(
    name := "Collectioneer Core",
    libraryDependencies ++= Seq(
    )
  )

lazy val cli = project
  .in(file("modules/cli"))
  .settings(commonSettings)
  .settings(
    name := "Collectioneer CLI",
    libraryDependencies ++= Seq(
      "com.github.scopt"        %% "scopt"            % "4.1.0",
      "io.circe"                %% "circe-core"       % "0.14.2",
      "io.circe"                %% "circe-generic"    % "0.14.2",
      "io.circe"                %% "circe-parser"     % "0.14.2",
      "io.circe"                %% "circe-yaml"       % "0.14.2",
    ),
    fork := true,
    mainClass := Some("com.oopman.collectioneer.cli.CLI")
  )
  .dependsOn(core)

lazy val repl = project
  .in(file("modules/repl"))
  .settings(commonSettings)
  .settings(
    name:= "Collectioneer REPL",
    libraryDependencies ++= Seq(
      "com.lihaoyi" % "ammonite" % "3.0.0-M0" % "test" cross CrossVersion.full
    )
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