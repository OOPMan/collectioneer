package com.oopman.collectioneer.plugins.gatcg.gui.menu

import com.oopman.collectioneer.gui.logback.TextAreaAppender
import com.typesafe.scalalogging.{LazyLogging, Logger}
import javafx.concurrent.Task
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ProgressBar, ScrollPane, TextArea}
import scalafx.scene.layout.VBox
import scalafx.stage.{Modality, Stage}

abstract class AbstractActionStage(parent: Stage) extends Stage:
  protected val task: Task[?] & LazyLogging

  protected val logTextArea = new TextArea:
    editable = false

  protected val progressBar = new ProgressBar:
    maxWidth <== logTextArea.width

  protected val closeCancelButton: Button = new Button("Cancel"):
    onAction = { event =>
      disable = true
      task.cancel()
    }

  lazy protected val appender = new TextAreaAppender(logTextArea)

  protected def configureLogger(logger: Logger): Unit =
    logger.underlying match
      case logger: ch.qos.logback.classic.Logger =>
        appender.setContext(logger.getLoggerContext)
        appender.start()
        logger.addAppender(appender)
      case _ => // Do Nothing

  protected def stopLogger(logger: Logger): Unit =
    logger.underlying match
      case logger: ch.qos.logback.classic.Logger =>
        appender.stop()
      case _ => // Do Nothing


  initOwner(parent)
  initModality(Modality.None)
  onCloseRequest = { event => task.cancel() }

  scene = new Scene(640, 480):
    root = new ScrollPane:
      fitToWidth = true
      fitToHeight = true
      content = new VBox(progressBar, logTextArea, closeCancelButton)

  override def close(): Unit =
    println("Killing appender")
    appender.stop()
    super.close()

  onShown = { event =>
    progressBar.progress <== task.progressProperty()
    task.setOnSucceeded { event =>
      logTextArea.scrollTop = Double.MaxValue
      closeCancelButton.disable = false
      closeCancelButton.text = "Close"
      closeCancelButton.onAction = { event => close() }
    }
    task.setOnCancelled { event => close() }
    val thread = new Thread(task)
    thread.setDaemon(true)
    thread.start()
  }

