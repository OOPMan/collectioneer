package com.oopman.collectioneer.plugins.gatcg.gui

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{ChoiceBox, Label, Tab, TabPane}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.util.StringConverter

import java.util.UUID

class GATCGCardDataTab(gatcgSubConfig: GATCGSubConfig, cardData: CardData, primaryEdition: Edition) extends Tab:
  val imageView = new ImageView

  val editionChoiceBox = new ChoiceBox[Edition]:
    def generateEditionLabel(edition: Edition): String =
      val rarity = GATCGRarities.fromRarity(edition.rarity)
      s"${edition.set.prefix} - ${edition.set.language} - ${edition.collectorNumber} - ${rarity.shortLabel}"

    items = ObservableBuffer.from(cardData.editions)
    // TODO: Multiple editions with different rarities are possible, support this more cleanly...
    converter = StringConverter(
      fromStringFunction = label => cardData.editions.find(edition => label == generateEditionLabel(edition)).head,
      toStringFunction = edition => generateEditionLabel(edition)
    )
    onAction = event => updateTab(selectionModel().getSelectedItem)
    selectionModel().select(primaryEdition)

  def updateTab(edition: Edition): Unit =
    // TODO: Update other data
    // Update displayed image
    // TODO: Handle cards with multiple images
    val imageUUID = UUID.nameUUIDFromBytes(edition.image.getBytes)
    val imagePath = s"${gatcgSubConfig.imagePath}/$imageUUID.jpg"
    imageView.image = new Image(os.Path(imagePath).getInputStream)

  text = "GATCG Card"
  content = new VBox:
    children = Seq(
      editionChoiceBox,
      new HBox:
        children = Seq(
          imageView,
          // Main Card Content Area
          new TabPane:
            tabs = Seq(
              new Tab:
                text = "Main"
                content = new Label:
                  text = "Main Card Text Area"
              ,
              new Tab:
                text = "Collector"
                content = new Label:
                  text = "Collector Data"
            )
          ,
          // Edition Data
          new VBox:
            children = Seq(
              new Label:
                text = "Current Edition Details"
              ,
              new Label:
                text = "Available Edition Links"
            )
        )
      ,
      new HBox:
        children = Seq(
          new Label:
            text = "Legality section"
          ,
          new Label:
            text = "Rules section"
        )
    )

