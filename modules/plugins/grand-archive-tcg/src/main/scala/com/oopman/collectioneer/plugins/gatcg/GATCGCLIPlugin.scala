package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.cli.{CLIConfig, CLISubConfig, Subject, Verb}
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.plugins.CLIPlugin
import com.oopman.collectioneer.plugins.gatcg.actions.{DownloadDataset, DownloadImages}
import com.oopman.collectioneer.{Injection, Plugin}
import com.typesafe.scalalogging.LazyLogging
import distage.ModuleDef
import io.circe.*
import io.circe.generic.auto.*
import io.circe.optics.JsonPath.*
import io.circe.parser.*
import io.circe.syntax.*
import izumi.distage.plugins.PluginDef
import scopt.{OParser, OParserBuilder}

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

  def importDataset(config: CLIConfig): Json =
    val subConfig = getSubConfigFromConfig(config)
    val datasetPath = subConfig.grandArchiveTCGJSON.map(os.FilePath.apply).map(p => os.Path(p, defaultRootPath)).getOrElse(defaultDatasetPath)

    class ImportDataset(datasetPath: os.Path,
                        collectionDAO: traits.dao.projected.CollectionDAO,
                        rawCollectionDAO: traits.dao.raw.CollectionDAO,
                        propertyDAO: traits.dao.projected.PropertyDAO,
                        propertyValueDAO: traits.dao.projected.PropertyValueDAO,
                        relationshipDAO: traits.dao.raw.RelationshipDAO)
      extends actions.ImportDataset(datasetPath, collectionDAO, rawCollectionDAO, propertyDAO, propertyValueDAO, relationshipDAO) with LazyLogging

    object importDatasetModule extends ModuleDef:
      make[os.Path].from(datasetPath)
      make[ImportDataset]

    val importDataset = Injection.produce[ImportDataset](importDatasetModule)
    importDataset()
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
