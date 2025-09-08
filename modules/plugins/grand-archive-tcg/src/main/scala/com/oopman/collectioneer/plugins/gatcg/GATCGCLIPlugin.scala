package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.cli.{CLIConfig, CLISubConfig, Subject, Verb}
import com.oopman.collectioneer.plugins.CLIPlugin
import com.oopman.collectioneer.plugins.gatcg.actions.{DownloadDataset, DownloadImages}
import com.oopman.collectioneer.{Injection, Plugin, SttpHelper}
import com.typesafe.scalalogging.LazyLogging
import distage.ModuleDef
import io.circe.*
import io.circe.generic.auto.*
import io.circe.optics.JsonPath.*
import io.circe.parser.*
import io.circe.syntax.*
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}
import sttp.client3.*
import sttp.client3.circe.*

import java.io.File
import java.util.UUID
import scala.language.postfixOps
import scala.util.*

case class DownloadDatasetResult
(
  datasetPath: Option[String] = None,
  datasetSize: Int = 0,
  downloadSucceeded: Boolean = true,
  errorMessage: Option[String] = None
)

class GATCGCLIPlugin extends CLIPlugin with LazyLogging:
  private val defaultRootPath = os.pwd / "gatcg"

  private val defaultDatasetPath = defaultRootPath / "gatcg.json"

  private val defaultImagesPath = defaultRootPath / "images"

  def getName: String = "Grand Archive TCG"

  def getShortName: String = "GATCG"

  def getVersion: String = "master"

  def getDefaultSubConfig: CLISubConfig = GATCGPluginConfig()

  // TODO: We should no longer need this
  def getSubConfigFromConfig(config: CLIConfig): GATCGPluginConfig =
    config.subConfigs.getOrElse(getShortName, getDefaultSubConfig).asInstanceOf[GATCGPluginConfig]

  def getActions(builder: OParserBuilder[CLIConfig]): List[(Verb, Subject, CLIConfig => Json, List[OParser[?, CLIConfig]])] =
    val datasetPathOpt = builder.opt[File]("gatcg-json-dataset-path")
      .optional()
      .action((f, config) => config.copy(subConfigs = config.subConfigs.updated(getShortName, getSubConfigFromConfig(config).copy(grandArchiveTCGJSON = Some(f)))))
      .text("Grand Archive TCG JSON Dataset file path")
    val imagesPathOpt = builder.opt[File]("gatcg-images-path")
      .optional()
      .action((f, config) => config.copy(subConfigs = config.subConfigs.updated(getShortName, getSubConfigFromConfig(config).copy(grandArchiveTCGImages = Some(f)))))
      .text("Grand Archive TCG Images folder path")
    List(
      (Verb.imprt, Subject.dataset, importDataset, List(datasetPathOpt, imagesPathOpt)),
      (Verb.download, Subject.dataset, downloadDataset, List(datasetPathOpt, imagesPathOpt)),
      (Verb("validate", ""), Subject.dataset, validateDataset, List(datasetPathOpt)),
      (Verb.download, Subject("images", Map.empty), downloadImages, List(datasetPathOpt, imagesPathOpt))
    )

  def getData
  (
    client: SimpleHttpClient = SimpleHttpClient(),
    baseUri: String = "https://api.gatcg.com",
    page: Int = 1,
    pageSize: Int = 50,
    delayBetweenRequests: Long = 500
  ): Try[Vector[Json]] =
    val uri = uri"$baseUri/cards/search?page=$page&page_size=$pageSize"
    val request = basicRequest
      .get(uri)
      .response(asJson[io.circe.Json])
    logger.info(s"Retrieving page $page with page size $pageSize from $uri")
    SttpHelper.sendRequest(client, request, delayBetweenRequests) match
      case Success(Response(Left(body), code, statusText, headers, history, request)) =>
        val message = s"Error downloading $page with page size $pageSize from $baseUri: $code"
        logger.error(message)
        Failure(RuntimeException(message))
      case Success(Response(Right(body), code, statusText, headers, history, request)) =>
        val hasMore = root.has_more.boolean.getOption(body).getOrElse(false)
        val data = root.data.arr.getOption(body).getOrElse(Vector())
        if !hasMore then
          Success(data)
        else
          this.synchronized { wait(delayBetweenRequests) }
          getData(client, baseUri, page + 1, pageSize).map(newData => data :++ newData)
      case Failure(exception) => Failure(exception)

  def importDataset(config: CLIConfig): Json =
    val subConfig = getSubConfigFromConfig(config)
    val pathOption = subConfig.grandArchiveTCGJSON.map(os.FilePath.apply).map(p => os.Path(p, defaultRootPath))
    val dataOption: Option[Vector[Json]] = pathOption
      .map(path => parse(os.read(path)))
      .map {
        case Left(parsingException) =>
          logger.error("Failed to parse GATCG JSON Dataset", parsingException)
          Vector()
        case Right(json) =>
          json.asArray.getOrElse(Vector())
      }
    val dataTry: Try[Json] = dataOption match {
      case Some(Vector()) =>
        val message = "GATCG JSON Dataset contains no data or the root element is not an Array"
        logger.error(message)
        Failure(RuntimeException(message))
      case Some(value) =>
        logger.info(s"Loaded ${value.length} items from GATCG JSON Dataset")
        Success(value.asJson)
      case None =>
        logger.warn("No GATCG JSON Dataset passed so data will be retrieved from the GATCG Index API")
        getData().map(_.asJson)
    }
    val modelsTry = dataTry.flatMap(data => {
      import Models.*
      data.as[List[Card]].toTry
    })
    val result = modelsTry.map(cards => {
      object importDatasetModule extends ModuleDef:
        make[List[Models.Card]].from(cards)

      Injection.produceRun(importDatasetModule)(actions.importDataset)
    })
    // TODO: Replace with a real response
    "Something".asJson

  def downloadDataset(config: CLIConfig): Json =
    val path = getSubConfigFromConfig(config).grandArchiveTCGJSON.map(os.Path.apply).getOrElse(defaultDatasetPath)
    val downloadDataset = new DownloadDataset(path) with LazyLogging
    val result = downloadDataset() match
      case Failure(e) => DownloadDatasetResult(downloadSucceeded = false, errorMessage = Some(e.getMessage))
      case Success(data) => DownloadDatasetResult(datasetPath = Some(path.toString), datasetSize = data.length)
    result.asJson

  def downloadImages(config: CLIConfig): Json =
    logger.info("Downloading GATCG Images")
    val subConfig = getSubConfigFromConfig(config)
    val datasetPath = subConfig.grandArchiveTCGJSON.map(os.FilePath.apply).map(p => os.Path(p, defaultRootPath)).getOrElse(defaultDatasetPath)
    val imagesPath = subConfig.grandArchiveTCGImages.map(os.FilePath.apply).map(p => os.Path(p, defaultRootPath)).getOrElse(defaultImagesPath)
    val downloadImages = new DownloadImages(datasetPath, imagesPath) with LazyLogging
    val result = downloadImages()
    "".asJson

  def validateDataset(config: CLIConfig): Json =
    logger.info("Validating GATCG Dataset")
    val subConfig = getSubConfigFromConfig(config)
    val datasetPath = subConfig.grandArchiveTCGJSON.map(os.FilePath.apply).map(p => os.Path(p, defaultRootPath)).getOrElse(defaultDatasetPath)
    val result =
      if !os.exists(datasetPath)
      then false
      else
        parse(os.read(datasetPath)) match
          case Left(failure) =>
            logger.error("Failed to read dataset", failure)
            false
          case Right(json) =>
            import Models.*
            json.as[List[Card]] match
              case Left(failure) =>
                logger.error("Failed to coerce dataset", failure)
                false
              case _ => true
    result.asJson

object GATCGCLIPluginDef extends PluginDef:
  many[CLIPlugin].add[GATCGCLIPlugin]
  many[Plugin].add[GATCGCLIPlugin]
