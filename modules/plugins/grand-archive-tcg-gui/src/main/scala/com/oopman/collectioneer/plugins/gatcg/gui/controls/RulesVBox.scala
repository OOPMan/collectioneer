package com.oopman.collectioneer.plugins.gatcg.gui.controls

import com.oopman.collectioneer.gui.StyleClasses
import com.oopman.collectioneer.plugins.gatcg.gui.{CardData, Edition}
import scalafx.geometry.Orientation.Horizontal
import scalafx.scene.Node
import scalafx.scene.control.{Label, Separator}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.{Text, TextFlow}

class RulesVBox(cardData: CardData, edition: Edition) extends VBox:
  val fieldLabel = "field-label"
  val rules = cardData.rules ++ edition.innerCards.flatMap(_.rules)
  val seqOfNodes: Seq[Seq[Node]] = for rule <- rules yield
    val titleOption =
      if rule.title.strip().isEmpty then None
      else Some(new HBox(new Label("Title:") with StyleClasses(fieldLabel), Label(" " + rule.title)))
    val dateOption =
      if rule.dateAdded.strip().isEmpty then None
      else Some(new HBox(new Label("Date Added:") with StyleClasses(fieldLabel), Label(" " + rule.dateAdded)))
    val descriptionOption =
      if rule.description.strip().isEmpty then None
      else Some(new TextFlow(new Text(rule.description)))
    val nodes = titleOption ++ dateOption ++ descriptionOption
    nodes.toSeq
  val nodes =
    if seqOfNodes.isEmpty
    then new Label("None") with StyleClasses(fieldLabel) :: Nil
    else seqOfNodes.reduceLeft((left, right) => (left :+ Separator(Horizontal)) ++ right)
  children = nodes

