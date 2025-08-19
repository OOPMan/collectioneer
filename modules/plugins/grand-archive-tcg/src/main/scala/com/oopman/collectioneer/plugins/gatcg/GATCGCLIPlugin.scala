package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.{Injection, Plugin}
import com.oopman.collectioneer.cli.{CLIConfig, CLISubConfig, Subject, Verb}
import com.oopman.collectioneer.plugins.CLIPlugin
import com.typesafe.scalalogging.LazyLogging
import distage.ModuleDef
import io.circe.*
import io.circe.generic.auto.*
import io.circe.optics.JsonPath.*
import io.circe.parser.*
import io.circe.syntax.*
import izumi.distage.plugins.PluginDef
import os.Path
import scopt.{OParser, OParserBuilder}
import sttp.client3.*
import sttp.client3.circe.*
import sttp.model.Uri

import java.io.{ByteArrayInputStream, File}
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
    logger.info(s"Retrieving page $page with page size $pageSize from $baseUri")
    val request = basicRequest
      .get(uri"$baseUri/cards/search?page=$page&page_size=$pageSize")
      .response(asJson[io.circe.Json])
    val response = client.send(request)
    response.body match
      case Left(body) =>
        val message = s"Error downloading $page with page size $pageSize from $baseUri: ${response.code}"
        logger.error(message)
        Failure(RuntimeException(message))
      case Right(body) =>
        val hasMore = root.has_more.boolean.getOption(body).getOrElse(false)
        val data = root.data.arr.getOption(body).getOrElse(Vector())
        if !hasMore then
          Success(data)
        else
          this.synchronized { wait(delayBetweenRequests) }
          getData(client, baseUri, page + 1, pageSize).map(newData => data :++ newData)

  def getAndSaveImages
  (
    data: Json,
    imagesPath: Path,
    client: SimpleHttpClient = SimpleHttpClient(),
    baseUri: String = "https://api.gatcg.com",
    delayBetweenRequests: Long = 500
  ): Int =
    import Models.*
    val cards = data.as[List[Card]].getOrElse(Nil)
    val images =
      for
        card <- cards
        edition <- card.editions
      yield
        val images =
          for
            innerCard <- edition.other_orientations.getOrElse(Nil)
          yield
            innerCard.edition.image
        images.prepended(edition.image)
    val uniqueImages = images.flatten.toSet
    var downloaded = 0
    for
      image <- uniqueImages
      imageHash = UUID.nameUUIDFromBytes(image.getBytes)
      imagePath = imagesPath / s"$imageHash.jpg"
      if !os.exists(imagePath)
    do Uri.parse(s"$baseUri$image") match
      case Left(value) =>
        logger.error(s"Failed to parse $baseUri$image to a Uri")
      case Right(uri) =>
        logger.info(s"Downloading $uri")
        val request = basicRequest
          .get(uri)
          .response(asByteArray)
        val response = client.send(request)
        response.body match
          case Left(value) =>
            logger.error(s"Failed to retrieve $baseUri$image")
          case Right(value) =>
            val inputStream = new ByteArrayInputStream(value)
            os.write(imagePath, inputStream)
            downloaded += 1
        this.synchronized {
          this.wait(delayBetweenRequests)
        }
    logger.info(s"Download $downloaded images out of ${uniqueImages.size}")
    downloaded

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
    logger.info("Downloading GATCG dataaset")
    val result = getData() match
      case Failure(e) => DownloadDatasetResult(downloadSucceeded = false, errorMessage = Some(e.getMessage))
      case Success(data) =>
        logger.info(s"Downloaded ${data.length} GATCG cards")
        val path = getSubConfigFromConfig(config).grandArchiveTCGJSON.map(os.Path.apply).getOrElse(defaultDatasetPath)
        val dataAsString = data.asJson.spaces2
        os.write(path, dataAsString)
        DownloadDatasetResult(datasetPath = Some(path.toString), datasetSize = data.length)
    result.asJson

  def downloadImages(config: CLIConfig): Json =
    logger.info("Downloading GATCG Images")
    val subConfig = getSubConfigFromConfig(config)
    val datasetPath = subConfig.grandArchiveTCGJSON.map(os.FilePath.apply).map(p => os.Path(p, defaultRootPath)).getOrElse(defaultDatasetPath)
    val imagesPath = subConfig.grandArchiveTCGImages.map(os.FilePath.apply).map(p => os.Path(p, defaultRootPath)).getOrElse(defaultImagesPath)
    if !os.exists(datasetPath) then return Json.Null
    if !os.exists(imagesPath) then os.makeDir.all(imagesPath)
    val json = parse(os.read(datasetPath)).getOrElse(Nil.asJson)
    val downloaded = getAndSaveImages(json, imagesPath)
    downloaded.asJson

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
