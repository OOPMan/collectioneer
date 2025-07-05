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
    items = ObservableBuffer.from(cardData.editions)
    converter = StringConverter(
      fromStringFunction = label => cardData.editions.find(edition => label.endsWith(s"(${edition.set.prefix})")).head,
      toStringFunction = edition => s"${edition.set.name} (${edition.set.prefix})"
    )
    onAction = event =>
      val edition = selectionModel().getSelectedItem
      val imageUUID = UUID.nameUUIDFromBytes(edition.image.getBytes)
      val imagePath = s"${gatcgSubConfig.imagePath}/$imageUUID.jpg"
      imageView.image = new Image(os.Path(imagePath).getInputStream)
    selectionModel().select(primaryEdition)

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

