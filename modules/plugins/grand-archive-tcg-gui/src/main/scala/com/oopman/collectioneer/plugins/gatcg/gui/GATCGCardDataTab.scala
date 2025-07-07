package com.oopman.collectioneer.plugins.gatcg.gui

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{ChoiceBox, Label, Tab, TabPane}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.util.StringConverter

import java.util.UUID

class GATCGCardDataTab(gatcgSubConfig: GATCGSubConfig, cardData: CardData, primaryEdition: Edition) extends Tab:
  val imageView = new ImageView

  def updateImageView(image: String): Unit =
    val imageUUID = UUID.nameUUIDFromBytes(image.getBytes)
    val imagePath = s"${gatcgSubConfig.imagePath}/$imageUUID.jpg"
    imageView.image = new Image(os.Path(imagePath).getInputStream)

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
    onAction = event => handleEditionChoiceBoxOnAction(selectionModel().getSelectedItem)

  def handleEditionChoiceBoxOnAction(edition: Edition): Unit =
    // Update orientationChoiceBox contents and visibility
    orientationChoiceBox.visible = edition.orientation.isDefined
    val orientations =
      for
        innerCard <- edition.innerCards
        orientation <- innerCard.innerEdition.orientation
      yield orientation.capitalize
    orientationChoiceBox.items = ObservableBuffer.from(edition.orientation.map(_.capitalize) ++ orientations)
    // TODO: Update other data
    // Update displayed image
    updateImageView(edition.image)

  val orientationChoiceBox = new ChoiceBox[String]:
    onAction = event => handleOrientationCheckBoxOnAction(selectionModel().getSelectedItem.toLowerCase)

  def handleOrientationCheckBoxOnAction(orientation: String): Unit =
    val edition = editionChoiceBox.selectionModel().getSelectedItem
    val image =
      if edition.orientation.contains(orientation) then edition.image
      else edition.innerCards.find(ic => ic.innerEdition.orientation.contains(orientation)).get.innerEdition.image
    updateImageView(image)

  editionChoiceBox.selectionModel().select(primaryEdition)
  for orientation <- primaryEdition.orientation do orientationChoiceBox.selectionModel().select(orientation.capitalize)

  text = "GATCG Card"
  content = new VBox:
    children = Seq(
      editionChoiceBox,
      new HBox:
        children = Seq(
          // Image area
          new VBox:
            children = Seq(
              imageView,
              orientationChoiceBox
            ),
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

