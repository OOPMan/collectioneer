package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.plugins.gatcg.gui.controls.CardDataVBox
import scalafx.collections.{ObservableBuffer, fillSFXCollectionWithOne}
import scalafx.scene.control.{ChoiceBox, Label, ScrollPane, Tab, TabPane}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.text.{Font, FontWeight, Text, TextAlignment}
import scalafx.util.StringConverter

import java.util.UUID

class GATCGCardDataTab(gatcgSubConfig: GATCGSubConfig, cardData: CardData, primaryEdition: Edition) extends Tab:
  val imageView = new ImageView
  val illustratorPrefixText = new Text:
    text = "Illustrator:"
    font = Font("Sytem", FontWeight.Normal, 12)

  val illustratorText = new Text:
    font = Font("Sytem", FontWeight.Bold, 12)

  //
  //  val mainGridPane = new GridPane:
  //    hgrow = Always
  //    vgrow = Always
  //    hgap = 4
  //    vgap = 4
  //    add(cardNameText, 0, 0, 2, 1)
  //    add(elementText, 2, 0, 2, 1)

  val ruleGridPane = new GridPane:
    hgrow = Always
    vgrow = Always
    hgap = 4
    vgap = 4
  val legalityGridPane = new GridPane:
    hgrow = Always
    vgrow = Always
    hgap = 4
    vgap = 4
  val collectorGridPane = new GridPane:
    hgrow = Always
    vgrow = Always
    hgap = 4
    vgap = 4

  val mainTab = new Tab:
    text = "Main"
    closable = false
  val rulesTab = new Tab:
    text = "Rules"
    content = ruleGridPane
    closable = false
  val legalityTab = new Tab:
    text = "Legality"
    content = legalityGridPane
    closable = false
  val collectorTab = new Tab:
    text = "Collector"
    content = collectorGridPane
    closable = false

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
    // TODO: Update ruleGridPane with data
    // TODO: Update legalityGridPane with data
    // TODO: Update collectorGridPane with data
    // Update displayed image
    updateImageView(edition.image)

  val orientationChoiceBox = new ChoiceBox[String]:
    onAction = event =>
      for orientation <- Option(selectionModel().getSelectedItem)
      do handleOrientationCheckBoxOnAction(orientation.toLowerCase)

  def handleOrientationCheckBoxOnAction(orientation: String): Unit =
    val edition = editionChoiceBox.selectionModel().getSelectedItem
    val image =
      if edition.orientation.contains(orientation) then edition.image
      else edition.innerCards.find(ic => ic.innerEdition.orientation.contains(orientation)).get.innerEdition.image
    updateImageView(image)

  editionChoiceBox.selectionModel().select(primaryEdition)

  text = "GATCG Card"
  content = new VBox:
    hgrow = Always
    vgrow = Always
    children = Seq(
      editionChoiceBox,
      new HBox:
        hgrow = Always
        vgrow = Always
        children = Seq(
          // Image area
          new VBox:
            hgrow = Always
            vgrow = Always
            children = Seq(
              imageView,
              new HBox(illustratorPrefixText, illustratorText),
              orientationChoiceBox
            ),
          // Main Card Content Area
          new TabPane:
            hgrow = Always
            vgrow = Always
            tabs = Seq(mainTab, rulesTab, legalityTab, collectorTab)
        )
    )

