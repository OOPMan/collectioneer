package com.oopman.collectioneer.gui.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import scalafx.scene.control.TextArea

class TextAreaAppender(textArea: TextArea) extends AppenderBase[ILoggingEvent]:
  override def append(eventObject: ILoggingEvent): Unit =
    javafx.application.Platform.runLater(() =>
      val currentText = textArea.text.value
      val newText = s"$currentText\n${eventObject.getFormattedMessage}".strip()
      textArea.text = newText
      textArea.scrollTop = Double.MaxValue
    )


