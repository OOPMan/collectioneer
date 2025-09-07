package com.oopman.collectioneer.plugins.gatcg.gui.menu

import com.oopman.collectioneer.plugins.gatcg.actions.DownloadDataset
import com.typesafe.scalalogging.LazyLogging
import io.circe.Json
import javafx.concurrent.Task
import scalafx.stage.Stage

import java.io.File
import scala.util.{Failure, Try}

object DownloadDatasetStage:
  def apply(path: File, parent: Stage) = new DownloadDatasetStage(path, parent)

class DownloadDatasetStage(path: File, parent: Stage) extends AbstractActionStage(parent):
  title = "Download GATCG Dataset"

  protected val task = new Task[Try[Vector[Json]]]() with DownloadDataset(os.Path(path)) with LazyLogging:

    override def call(): Try[Vector[Json]] =
      configureLogger(logger)
      apply()

    override def getData(page: Int): Try[Vector[Json]] =
      for totalPages <- totalPages do updateProgress(page, totalPages)
      if isCancelled then Failure(RuntimeException("Cancelled"))
      else super.getData(page)

