package com.oopman.collectioneer.cli

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.io.File
import java.util.UUID
import scala.collection.immutable.Map.from

case class Verb
(
  name: String,
  help: String
)

object Verb:
  def apply(name: String, help: String) =
    new Verb(name.toLowerCase, help)

  val create = Verb("create", "Create objects within the Database")
  val delete = Verb("delete", "Delete objects from the Database")
  val update = Verb("update", "Update objects within the Database")
  val list = Verb("list", "List objects wtihin the Database")
  val get = Verb("get", "Retrieve objects from the Database")
  val imprt = Verb("import", "Import objects into the Database")

case class Subject
(
  name: String,
  help: Map[Verb, String]
)

object Subject:
  def apply(name: String, help: Map[Verb, String]) =
    new Subject(name.toLowerCase, help)

  val database = Subject("database", Map())
  val collections = Subject("collections", Map())
  val properties = Subject("properties", Map())

enum OutputFormat:
  case json extends OutputFormat
  case yaml extends OutputFormat

// TODO: Add sub-config classes for verbs
trait Subconfig

case class Config
(
  action: Config => Json = config => "".asJson,
  verb: Option[Verb] = None,
  subject: Option[Subject] = None,
  datasourceUri: String = "jdbc:h2:./collection",
  datasourceUsername: String = "sa",
  datasourcePassword: String = "",
  verbose: Boolean = false,
  debug: Boolean = false,
  outputFormat: OutputFormat = OutputFormat.yaml,
  uuids: List[UUID] = Nil,
  usePlugin: Option[String] = None,
  subconfigs: Map[String, Subconfig] = Map(),
)
