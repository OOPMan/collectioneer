package com.oopman.collectioneer.plugins.gatcg.gui.menu

import com.oopman.collectioneer.gui.logback.TextAreaAppender
import com.oopman.collectioneer.plugins.gatcg.actions.DownloadDataset
import com.typesafe.scalalogging.LazyLogging
import io.circe.Json
import javafx.concurrent.Task
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ProgressBar, ScrollPane, TextArea}
import scalafx.scene.layout.VBox
import scalafx.stage.{Modality, Stage}

import java.io.File
import scala.util.{Failure, Try}

class DownloadDatasetStage(path: File, parent: Stage) extends Stage with LazyLogging:
  private val task = new Task[Try[Vector[Json]]]() with DownloadDataset(os.Path(path)) with LazyLogging:
    override def succeeded(): Unit =
      closeCancelButton.disable = false
      closeCancelButton.text = "Close"
      closeCancelButton.onAction = { event => close() }

    override def cancelled(): Unit = close()

    override def call(): Try[Vector[Json]] =
      logger.underlying match
        case logger: ch.qos.logback.classic.Logger =>
          val appender = new TextAreaAppender(logTextArea)
          appender.setContext(logger.getLoggerContext)
          appender.start()
          logger.addAppender(appender)
        case _ => // Do Nothing
      apply()

    override def getData(page: Int): Try[Vector[Json]] =
      for totalPages <- totalPages do updateProgress(page, totalPages)
      if isCancelled then Failure(RuntimeException("Cancelled"))
      else super.getData(page)

  private val thread = new Thread(task)
  thread.setDaemon(true)

  private val logTextArea = new TextArea:
    editable = false

  private val progressBar = new ProgressBar:
    maxWidth <== logTextArea.width
    progress <== task.progressProperty()

  private val closeCancelButton: Button = new Button("Cancel"):
    onAction = { event =>
      disable = true
      task.cancel()
    }

  title = "Download Dataset"
  initOwner(parent)
  initModality(Modality.None)
  onCloseRequest = { event => task.cancel() }
  scene = new Scene(640, 480):
    root = new ScrollPane:
      fitToWidth = true
      fitToHeight = true
      content = new VBox(progressBar, logTextArea, closeCancelButton)

  show()
  thread.start()


