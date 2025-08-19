package com.oopman.collectioneer.plugins.gatcg.gui.controls

import com.oopman.collectioneer.gui.StyleClasses
import com.oopman.collectioneer.plugins.gatcg.gui.Circulation
import scalafx.geometry.Orientation.Horizontal
import scalafx.scene.Node
import scalafx.scene.control.{Label, Separator}
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Text, TextFlow}

class CirculationsVBox(circulations: Seq[Circulation]) extends VBox:
  val fieldLabel = "field-label"
  val seqOfSeqOfNodes: Seq[Seq[Node]] = for circulation <- circulations yield
    val nameOption = circulation.name.headOption
    val circulationNameOption = for name <- nameOption yield new TextFlow(
      new Text("Circulation:") with StyleClasses(fieldLabel), Text(" " + name)
    )
    circulationNameOption ++: Seq(
      new TextFlow(new Text("Population:") with StyleClasses(fieldLabel), Text(s" ${circulation.populationOperator} ${circulation.population}")),
      new TextFlow(new Text("Foil:") with StyleClasses(fieldLabel), Text(if circulation.foil then " Yes" else " No"))
    )
  val nodes =
    if seqOfSeqOfNodes.isEmpty
    then new Label("None") with StyleClasses(fieldLabel) :: Nil
    else seqOfSeqOfNodes.reduceLeft((left, right) => (left :+ Separator(Horizontal)) ++ right)
  children = nodes


