package com.oopman.collectioneer.gui.logback

import akka.stream.*
import akka.stream.scaladsl.*
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import com.oopman.collectioneer.Implicits.*
import scalafx.scene.control.TextArea

import scala.concurrent.*
import scala.concurrent.duration.*
import scala.util.Try

/**
 * By default, one batch of up to 100 messages will be processed and output to the TextArea every 1 milliseconds
 *
 * @param textArea A TextArea to output logging messages to
 * @param queueLimit Determines the maximum number of logging messages that may be queued for processing by the TextAreaAppender
 * @param batchSize Determines the maximum number of messages that will be grouped together into a batch for outputting to the TextArea
 * @param throttle Determines the throttling of how regularly batches will be processed
 */
class TextAreaAppender(textArea: TextArea, queueLimit: Int = 10000, batchSize: Int = 1000, throttle: FiniteDuration = 100.milli) extends AppenderBase[ILoggingEvent]:
  private val messages = Source
    .queue[String](queueLimit)
    .batch(batchSize, message => message :: Nil)((messages, message) => messages :+ message)
    .throttle(1, throttle)
    .to(Sink.foreach(messages => javafx.application.Platform.runLater(() =>
      val currentText = textArea.text.value
      val textToAdd = messages.mkString("\n")
      val newText = s"$currentText\n$textToAdd".strip()
      textArea.text = newText
      textArea.scrollTop = Double.MaxValue
    )))
    .run()

  override def stop(): Unit =
    Try(messages.complete())
    super.stop()

  override def append(eventObject: ILoggingEvent): Unit =
    messages.offer(eventObject.getFormattedMessage)
