package com.oopman.collectioneer.plugins.gatcg.gui.menu

import com.oopman.collectioneer.plugins.gatcg.actions.DownloadImages
import com.typesafe.scalalogging.LazyLogging
import javafx.concurrent.Task
import os.Path
import scalafx.stage.Stage
import sttp.model.Uri

import java.io.File
import scala.util.{Failure, Try}

object DownloadImagesStage:
  def apply(datasetPath: File, imagesPath: File, parent: Stage) =
    new DownloadImagesStage(datasetPath, imagesPath, parent)

class DownloadImagesStage(datasetPath: File, imagesPath: File, parent: Stage) extends AbstractActionStage(parent):
  title = "Download GATCG Card Images"

  protected val task = new Task[Set[Try[Unit]]] with DownloadImages(os.Path(datasetPath), os.Path(imagesPath)) with LazyLogging:
    override def call(): Set[Try[Unit]] =
      configureLogger(logger)
      apply()

    override def saveUriToPath(uri: Uri, path: Path): Try[Unit] =
      updateProgress(imagesDownloaded + failedDownloads, totalImages)
      if isCancelled then Failure(RuntimeException("Cancelled"))
      else super.saveUriToPath(uri, path)



