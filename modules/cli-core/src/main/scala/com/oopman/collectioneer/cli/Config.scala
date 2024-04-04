package com.oopman.collectioneer.cli

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.util.UUID

case class Verb
(
  name: String,
  help: String
)

object Verb:
  def apply(name: String, help: String) =
    new Verb(name.toLowerCase, help)

  val create = Verb("create", "Create operations using the Database")
  val delete = Verb("delete", "Delete operations using the Database")
  val update = Verb("update", "Update operations using the Database")
  val list = Verb("list", "List operations using the Database")
  val get = Verb("get", "Retrieve operations using the Database")
  val imprt = Verb("import", "Import operations using the Database")
  val exprt = Verb("export", "Export operations using the Database")
  val download = Verb("download", "Download operations")

case class Subject
(
  name: String,
  help: Map[Verb, String]
)

object Subject:
  def apply(name: String, help: Map[Verb, String]) =
    new Subject(name.toLowerCase, help)

  val dataset = Subject("dataset", Map())
  val database = Subject("database", Map())
  val collections = Subject("collections", Map())
  val properties = Subject("properties", Map())
  val propertyValues = Subject("property-values", Map())
  val relationships = Subject("relationships", Map())
  val relationshipCollections = Subject("relationship-collections", Map())
  val plugins = Subject("plugins", Map())

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
  datasourceUri: String = "jdbc:embeddedpostgresql://./collection",
  datasourceUsername: String = "sa",
  datasourcePassword: String = "",
  verbose: Boolean = false,
  debug: Boolean = false,
  outputFormat: OutputFormat = OutputFormat.yaml,
  uuids: List[UUID] = Nil,
  importDatasourceUris: List[String] = Nil,
  deleted: Option[Boolean] = None,
  virtual: Option[Boolean] = None,
  usePlugin: Option[String] = None,
  subconfigs: Map[String, Subconfig] = Map(),
) extends com.oopman.collectioneer.Config
