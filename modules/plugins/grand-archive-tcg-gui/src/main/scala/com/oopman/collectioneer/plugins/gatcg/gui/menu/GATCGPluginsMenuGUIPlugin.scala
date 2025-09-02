package com.oopman.collectioneer.plugins.gatcg.gui.menu

import com.oopman.collectioneer.plugins.{GUIPlugin, PluginsMenuGUIPlugin}
import distage.Id
import scalafx.scene.control.{Menu, MenuItem}
import scalafx.stage.{FileChooser, Stage}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GATCGPluginsMenuGUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), PluginsMenuGUIPlugin:
  private lazy val gatcgJSONFileChooser = new FileChooser:
    initialDirectory = os.home.toIO
    initialFileName = s"gatcg.${LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)}.json"

  private lazy val downloadDatasetMenuItem = new MenuItem("Download Dataset"):
    onAction = event => {
      for path <- Option(gatcgJSONFileChooser.showSaveDialog(stage))
      do new DownloadDatasetStage(path, stage)
    }

  private lazy val importDatasetMenuItem = new MenuItem("Import Dataset"):
    onAction = event => {
      // TODO: Handle click
      /**
       * Step 1: Show file chooser dialog (on cancel, do nothing further)
       * Step 2: Show new window that imports the dataset from the chosen file, displaying progress
       */
    }

  private lazy val downloadImagesMenuItem = new MenuItem("Download Images"):
    onAction = event => {
      // TODO: Handle click
      /**
       * Step 1: Show directory chooser dialog (on cancel, do nothing further)
       * Step 2: Show new window that downloads images, displaying progress
       */
    }

  def getMenu: Menu = new Menu("Grand Archive TCG"):
    items = downloadDatasetMenuItem :: importDatasetMenuItem :: downloadImagesMenuItem :: Nil

  override def getName: String = "GATCG Plugins Menu GUI Plugin"

  override def getShortName: String = "GATCGPluginsMenuGUI"

  override def getVersion: String = "master"

