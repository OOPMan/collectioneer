package com.oopman.collectioneer.plugins.gatcg.gui.menu

import scalafx.scene.Scene
import scalafx.scene.control.{Button, ProgressBar, ScrollPane, TextArea}
import scalafx.scene.layout.VBox
import scalafx.stage.{Modality, Stage}

import java.io.File

class DownloadDatasetStage(path: File, parent: Stage) extends Stage:
  lazy val progressBar = new ProgressBar
  lazy val logTextArea = new TextArea("Logging goes here...\nMore here\nWe might need a scrollbar\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nMore text"):
    editable = false
  lazy val closeCancelButton = new Button("Cancel")
  // TODO: Need on-click handler

  title = "Download Dataset"
  initOwner(parent)
  initModality(Modality.None)
  onCloseRequest = { event =>
    // TODO: Stop download if in progress
  }
  scene = new Scene(640, 480):
    root = new ScrollPane:
      fitToWidth = true
      fitToHeight = true
      content = new VBox(progressBar, logTextArea, closeCancelButton)
  show()
  // TODO: After showing the window we need to download the files. Needs to run in separate thread via Task system


