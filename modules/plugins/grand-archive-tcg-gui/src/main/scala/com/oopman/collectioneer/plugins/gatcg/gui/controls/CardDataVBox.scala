package com.oopman.collectioneer.plugins.gatcg.gui.controls

import com.oopman.collectioneer.plugins.gatcg.gui.controls.text.{Underline, Font}
import com.oopman.collectioneer.plugins.gatcg.gui.{CardCommon, CardData, Edition, EditionCommon}
import scalafx.scene.Node
import scalafx.scene.control.TitledPane
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.{FontWeight, Text, TextAlignment}

class CardDataVBox(val cardData: CardData, val edition: Edition) extends VBox:

  def generateNode(cardData: CardCommon, edition: EditionCommon): Node =
    val cardNameText = new Text(cardData.name) with Font(14, FontWeight.Bold) with Underline
    val elementText = new Text(cardData.element) with Font(14, FontWeight.Bold)
    val types = new Text(cardData.types.mkString(" ")) with Font(weight = FontWeight.Bold)
    val subTypes = new Text(cardData.subTypes.mkString(" ")) with Font(weight = FontWeight.Bold)

    new TitledPane:
      text = edition.orientation.getOrElse("Front")
      expanded = true
      content = new VBox(
        new HBox(cardNameText, elementText),
        new HBox(types, new Text(" - "), subTypes)
      )

  children = generateNode(cardData, edition) +: edition.innerCards.map(innerCard => generateNode(innerCard, innerCard.innerEdition))

