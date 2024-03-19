package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.cli.{Config, Subconfig, Subject, Verb}
import com.oopman.collectioneer.db.h2.H2DatabaseBackend
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.plugins.CLIPlugin
import io.circe.*
import io.circe.optics.JsonPath.*
import io.circe.syntax.*
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}
import sttp.client3.*
import sttp.client3.circe.*

import scala.language.postfixOps

class GATCGCLIPlugin extends CLIPlugin:
  def getName: String = "Grand Archive TCG"

  def getShortName: String = "GATCG"

  def getVersion: String = "master"

  def getMigrationLocations(databaseBackend: DatabaseBackend): Seq[String] = databaseBackend match
    case _: H2DatabaseBackend => Seq("classpath:com/oopman/collectioneer/plugins/gatcg/migrations/h2")

  def getDefaultSubconfig: Subconfig = GATCGPluginConfig()

  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Config => Json, List[OParser[_, Config]])] =
    List(
      (Verb.imprt, Subject.database, importDatabase, List()),
    )

  def importDatabase(config: Config) =
//    val propertiesDAO = new PropertyDAO(config.datasourceUri)
//    val properties = GATCGProperties.values.toList.map(_.property)
//    propertiesDAO.createOrUpdateProperties(properties)
    // TODO: Replace with a real response
    "Something".asJson

  def downloadDatabase(config: Config) =
    val client = SimpleHttpClient()
    def getData(page: Int = 1, pageSize: Int = 50, delayBetweenRequests: Long = 500): Vector[Json] =
      // TODO: Print progress
      wait(delayBetweenRequests)
      val request = basicRequest
        .get(uri"https://api.gatcg.com/cards/search?page=$page&page_size=$pageSize")
        .response(asJson[io.circe.Json])
      val response = client.send(request)
      response.body match
        case Left(body) =>
          // TODO: Log errors
          Vector()
        case Right(body) =>
          val hasMore = root.has_more.boolean.getOption(body).getOrElse(false)
          val data = root.data.arr.getOption(body).getOrElse(Vector())
          if hasMore then data :++ getData(page + 1, pageSize) else data
    val data = getData()
    val s = data.asJson.spaces2SortKeys
    // TODO: Save data to file
//      response
//        .body
//        .map(json => {
//          val hasMore = root.has_more.boolean.getOption(json).getOrElse(false)
//          val data = root.data.arr.getOption(json).getOrElse(Vector())
//          data
//          if hasMore:
//            val z = "Something".asJson
//            // TODO: Recurive call
//          data
//        })



    "Something".asJson

object GATCGCLIPlugin extends PluginDef:
  many[CLIPlugin].add(GATCGCLIPlugin())
  many[Plugin].add(GATCGCLIPlugin())
