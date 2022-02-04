lazy val scala213Version      = "2.13.7"
lazy val scala30Version       = "3.1.0"

lazy val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := scala30Version,
  crossScalaVersions := Seq(scala213Version, scala30Version),
  libraryDependencies ++= Seq(
    "com.novocode"  % "junit-interface" % "0.11" % "test",
//    "com.lihaoyi" % "ammonite" % "2.5.2" cross CrossVersion.full
  ),
  Test / sourceGenerators += Def.task {
    val file = (Test / sourceManaged).value / "amm.scala"
    IO.write(file,
      """object amm {
        |   def main(args: Array[String]) = ammonite.Main.main(args)
        |}""".stripMargin
    )
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
      "org.flywaydb"            % "flyway-core"       % "6.3.0",
      "com.h2database"          % "h2"                % "1.4.200"  % "test",
      "org.tpolecat"            %% "doobie-core"      % "1.0.0-RC1",
      "org.tpolecat"            %% "doobie-h2"        % "1.0.0-RC1",          // H2 extensions support
      "org.tpolecat"            %% "doobie-hikari"    % "1.0.0-RC1",          // HikariCP transactor
      "org.tpolecat"            %% "doobie-munit"     % "1.0.0-RC1" % "test", // MUnit
    )
  )

lazy val cli = project
  .in(file("modules/cli"))
  .settings(commonSettings)
  .settings(
    name := "Collectioneer CLI",
    libraryDependencies ++= Seq(
      "com.github.scopt"        %% "scopt"            % "4.0.1",
    )
  )
  .dependsOn(core)

lazy val repl = project
  .in(file("modules/repl"))
  .settings(commonSettings)
  .settings(
    name:= "Collectioneer REPL",
    libraryDependencies ++= Seq(
//      "com.lihaoyi" % "ammonite" % "2.5.2" cross CrossVersion.full
    )
  )

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}
// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
lazy val gui = project
  .in(file("modules/gui"))
  .settings(commonSettings)
  .settings(
    name := "Collectioneer GUI",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature"),
    fork := true,
    mainClass := Some("ScalaFXHelloWorld"),
    libraryDependencies ++= Seq(
      "org.scalafx"             %% "scalafx"          % "17.0.1-R26",
    ),
    libraryDependencies ++= javaFXModules.map(m =>
      "org.openjfx" % s"javafx-$m" % "17.0.1" classifier osName
    )
  )
  .dependsOn(core)