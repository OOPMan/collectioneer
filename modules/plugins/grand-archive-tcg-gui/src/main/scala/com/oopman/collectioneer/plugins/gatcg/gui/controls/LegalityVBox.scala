package com.oopman.collectioneer.plugins.gatcg.gui.controls

import com.oopman.collectioneer.gui.StyleClasses
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Text, TextFlow}
import scalafx.scene.Node
import scalafx.scene.control.Label

class LegalityVBox(legality: Option[io.circe.Json]) extends VBox:
  stylesheets.add("css/gatcg-ui.css")

  private val textFlowsOption =
    for
      json <- legality
      cursor = json.hcursor
      formats <- cursor.keys
    yield
      val textFlows = for
        format <- formats
        limitCursor = cursor.downField(format).downField("limit")
        limit = limitCursor.as[Int].getOrElse(0)
      yield
        new TextFlow(
          new Text(format.toLowerCase.capitalize + ":") with StyleClasses("field-label"),
          new Text(" " + limit.toString)
        )
      textFlows
  private val nodes: Iterable[Node] = textFlowsOption.getOrElse(new Label("No restrictions") with StyleClasses("field-label") :: Nil)

  children = nodes.toSeq

