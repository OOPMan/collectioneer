package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.plugins.gatcg.gui.controls.{CardDataVBox, LegalityVBox}
import scalafx.collections.{ObservableBuffer, fillSFXCollectionWithOne}
import scalafx.scene.control.{ChoiceBox, Label, ScrollPane, Tab, TabPane}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.Priority
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.text.{Font, FontWeight, Text, TextAlignment}
import scalafx.util.StringConverter

import java.util.UUID

class GATCGCardDataTab(gatcgSubConfig: GATCGSubConfig, cardData: CardData, primaryEdition: Edition) extends Tab:
  
  val imageView = new ImageView
  val illustratorPrefixLabel = new Label:
    text = "Illustrator:"
    font = Font("System", FontWeight.Normal, 12)

  val illustratorLabel = new Label:
    font = Font("System", FontWeight.Bold, 12)

  val mainTab = new Tab:
    text = "Main"
    closable = false
  val rulesTab = new Tab:
    text = "Rules"
    closable = false
  val legalityTab = new Tab:
    text = "Legality"
    closable = false
  val collectorTab = new Tab:
    text = "Collector"
    closable = false

  def updateImageView(image: String, illustrator: Option[String]): Unit =
    val imageUUID = UUID.nameUUIDFromBytes(image.getBytes)
    val imagePath = s"${gatcgSubConfig.imagePath}/$imageUUID.jpg"
    imageView.image = new Image(os.Path(imagePath).getInputStream)
    illustratorLabel.text = illustrator.getOrElse("")

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
    val orientations =
      for
        innerCard <- edition.innerCards
        orientation <- innerCard.innerEdition.orientation
      yield orientation.capitalize
    orientationChoiceBox.items = ObservableBuffer.from(edition.orientation.map(_.capitalize) ++ orientations)
    orientationChoiceBox.visible = edition.orientation match
      case Some(orientation) =>
        orientationChoiceBox.selectionModel().select(orientation.capitalize)
        true
      case None => false
    // TODO: Update mainGridPane with data
    val cardDataVBox = new CardDataVBox(cardData, edition)
    mainTab.content = cardDataVBox
    legalityTab.content = new LegalityVBox(cardData.legality)
    // TODO: Update ruleGridPane with data
    // TODO: Update legalityGridPane with data
    // TODO: Update collectorGridPane with data
    // Update displayed image
    updateImageView(edition.image, edition.illustrator)

  val orientationChoiceBox = new ChoiceBox[String]:
    onAction = event =>
      for orientation <- Option(selectionModel().getSelectedItem)
      do handleOrientationCheckBoxOnAction(orientation.toLowerCase)

  def handleOrientationCheckBoxOnAction(orientation: String): Unit =
    val edition = editionChoiceBox.selectionModel().getSelectedItem
    val (image, illustrator) =
      if edition.orientation.contains(orientation)
      then (edition.image, edition.illustrator)
      else
        val innerEdition = edition.innerCards.find(ic => ic.innerEdition.orientation.contains(orientation)).get.innerEdition
        (innerEdition.image, innerEdition.illustrator)
    updateImageView(image, illustrator)

  editionChoiceBox.selectionModel().select(primaryEdition)

  text = "GATCG Card"
  content = new VBox:
    stylesheets.add("css/gatcg-ui.css")
    hgrow = Priority.Always
    vgrow = Priority.Always
    children = Seq(
      editionChoiceBox,
      new HBox:
        hgrow = Priority.Always
        vgrow = Priority.Always
        children = Seq(
          // Image area
          new VBox:
            hgrow = Priority.Never
            vgrow = Priority.Always
            children = Seq(
              imageView,
              new HBox(illustratorPrefixLabel, illustratorLabel),
              orientationChoiceBox
            ),
          // Main Card Content Area
          new TabPane:
            hgrow = Priority.Always
            vgrow = Priority.Always
            tabs = Seq(mainTab, rulesTab, legalityTab, collectorTab)
        )
    )

