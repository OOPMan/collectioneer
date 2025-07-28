package com.oopman.collectioneer.plugins.gatcg.gui.controls

import com.oopman.collectioneer.gui.StyleClasses
import com.oopman.collectioneer.plugins.gatcg.gui.{CardCommon, CardData, Edition, EditionCommon}
import scalafx.geometry.Orientation
import scalafx.scene.Node
import scalafx.scene.control.{Separator, TitledPane}
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Text, TextFlow}


class CardDataVBox(val cardData: CardData, val edition: Edition) extends VBox:
  stylesheets.add("css/carddatavbox.css")
  
  val cardDataLabelStyleClass = "card-data-label"

  def generateTextFlow(label: String, text: String): TextFlow =
    new TextFlow(
      new Text(label + ":") with StyleClasses(cardDataLabelStyleClass),
      new Text(" " + text)
    )

  def generateNode(cardData: CardCommon, edition: EditionCommon): Node =
    val effects = edition.effect ++ cardData.effect
    val effect = effects.headOption.map(effect => Separator(Orientation.Horizontal) :: new TextFlow(new Text(effect)) :: Nil).getOrElse(Nil)
    val nodeOptions: List[Option[Node]] =
      Some(generateTextFlow("Name", cardData.name)) ::
      Some(generateTextFlow("Element", cardData.element)) ::
      cardData.level.map(level => generateTextFlow("Level", level.toString)) ::
      Some(generateTextFlow("Classes", cardData.classes.mkString(" "))) ::
      Some(generateTextFlow("Types", cardData.types.mkString(" "))) ::
      Some(generateTextFlow("Sub-types", cardData.subTypes.mkString(" "))) ::
      cardData.reserveCost.map(cost => generateTextFlow("Reserve Cost", cost.toString)) ::
      cardData.memoryCost.map(cost => generateTextFlow("Memory Cost", cost.toString)) ::
      cardData.power.map(power => generateTextFlow("Power", power.toString)) ::
      cardData.life.map(life => generateTextFlow("Life", life.toString)) ::
      cardData.durability.map(durability => generateTextFlow("Durability", durability.toString)) ::
      cardData.speed.map(speed => generateTextFlow("Speed", speed)) ::
      Nil
    val nodes = nodeOptions.flatten ++ effect

    val cardNameLabelText = new Text("Name:") with StyleClasses(cardDataLabelStyleClass)
    val cardNameText = new Text(" " + cardData.name)
    val elementLabelText = new Text("Element:") with StyleClasses(cardDataLabelStyleClass)
    val elementText = new Text(" " + cardData.element)
    val typesLabelText = new Text("Types:") with StyleClasses(cardDataLabelStyleClass)
    val typesText = new Text(" " + cardData.types.mkString(" "))
    val subtypesLabelText = new Text("Subtypes:") with StyleClasses(cardDataLabelStyleClass)
    val subTypesText = new Text(" " + cardData.subTypes.mkString(" "))
    new TitledPane:
      text = edition.orientation.getOrElse("front").capitalize
      expanded = true
      content = new VBox(nodes*)

  children = generateNode(cardData, edition) +: edition.innerCards.map(innerCard => generateNode(innerCard, innerCard.innerEdition))

