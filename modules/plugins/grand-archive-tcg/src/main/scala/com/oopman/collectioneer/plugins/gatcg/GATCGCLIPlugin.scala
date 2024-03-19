package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.cli.{Config, Subconfig, Subject, Verb}
import com.oopman.collectioneer.db.h2.H2DatabaseBackend
import com.oopman.collectioneer.db.traits.DatabaseBackend
import com.oopman.collectioneer.plugins.CLIPlugin
import com.typesafe.scalalogging.LazyLogging
import io.circe.*
import io.circe.optics.JsonPath.*
import io.circe.syntax.*
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}
import sttp.client3.*
import sttp.client3.circe.*

import scala.language.postfixOps

class GATCGCLIPlugin extends CLIPlugin with LazyLogging:
  def getName: String = "Grand Archive TCG"

  def getShortName: String = "GATCG"

  def getVersion: String = "master"

  def getMigrationLocations(databaseBackend: DatabaseBackend): Seq[String] = databaseBackend match
    case _: H2DatabaseBackend => Seq("classpath:com/oopman/collectioneer/plugins/gatcg/migrations/h2")

  def getDefaultSubconfig: Subconfig = GATCGPluginConfig()

  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Config => Json, List[OParser[_, Config]])] =
    List(
      (Verb.imprt, Subject.database, importDatabase, List()),
      // TODO: Need a better verb?
      (Verb("download", ""), Subject.database, downloadDatabase, List())
    )

  def getData
  (
    client: SimpleHttpClient = SimpleHttpClient(),
    baseUri: String = "https://api.gatcg.com",
    page: Int = 1,
    pageSize: Int = 50,
    delayBetweenRequests: Long = 500
  ): Vector[Json] =
    logger.info(s"Retrieving page $page with page size $pageSize from $baseUri")
    val request = basicRequest
      .get(uri"$baseUri/cards/search?page=$page&page_size=$pageSize")
      .response(asJson[io.circe.Json])
    val response = client.send(request)
    response.body match
      case Left(body) =>
        logger.error(s"Error downloading $page with page size $pageSize from $baseUri: ${response.code}")
        Vector()
      case Right(body) =>
        val hasMore = root.has_more.boolean.getOption(body).getOrElse(false)
        val data = root.data.arr.getOption(body).getOrElse(Vector())
        if hasMore then
          this.synchronized {
            wait(delayBetweenRequests)
          }
          data :++ getData(client, baseUri, page + 1, pageSize)
        else data

  def importDatabase(config: Config) =
//    val propertiesDAO = new PropertyDAO(config.datasourceUri)
//    val properties = GATCGProperties.values.toList.map(_.property)
//    propertiesDAO.createOrUpdateProperties(properties)
    // TODO: Replace with a real response
    "Something".asJson

  def downloadDatabase(config: Config) =
    logger.info("Downloading GATCG dataaset")
    val data = getData()
    logger.info(s"Download ${data.length} GATCG cards")
    val s = data.asJson.spaces2SortKeys
    // TODO: Save data to file
    val workingDirectory = os.pwd
    os.write(workingDirectory / "test.json", s)
    // TODO: Return useful data
    "Something".asJson

object GATCGCLIPlugin extends PluginDef:
  many[CLIPlugin].add(GATCGCLIPlugin())
  many[Plugin].add(GATCGCLIPlugin())
