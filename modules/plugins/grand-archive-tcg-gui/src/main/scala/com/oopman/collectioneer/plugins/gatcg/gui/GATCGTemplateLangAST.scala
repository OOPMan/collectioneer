package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.gui.StyleClasses
import scalafx.scene.Node
import scalafx.scene.layout.StackPane
import scalafx.scene.text.Text

object GATCGTemplateLangAST:
  private def childrenToStyledNodes(children: Seq[AST], styleClass: String): Seq[Node] =
    val nodes = children.flatMap(_.toNodes)
    nodes.foreach(_.styleClass += styleClass)
    nodes

  sealed trait AST:
    def index: Int
    def toNodes: Seq[Node]

  case class RegularText(index: Int, text: String) extends AST:
    def toNodes = Text(text) :: Nil

  case class BoldText(index: Int, children: Seq[AST]) extends AST:
    def toNodes = childrenToStyledNodes(children, GATCGUICSS.boldText)

  case class ItalicText(index: Int, children: Seq[AST]) extends AST:
    def toNodes = childrenToStyledNodes(children, GATCGUICSS.italicText)

  case class BubbleText(index: Int, childASTs: Seq[AST]) extends AST:
    def toNodes =
      val outerNode = new StackPane with StyleClasses(GATCGUICSS.bubbleTextBackground):
        children = childrenToStyledNodes(childASTs, GATCGUICSS.bubbleText)
      outerNode :: Nil

  case class Life(index: Int) extends AST:
    def toNodes = new Text("Life") with StyleClasses(GATCGUICSS.lifeText) :: Nil

  case class Power(index: Int) extends AST:
    def toNodes = new Text("Power") with StyleClasses(GATCGUICSS.powerText) :: Nil

  case class Rest(index: Int) extends AST:
    def toNodes = new Text("Rest") with StyleClasses(GATCGUICSS.restText) :: Nil

  case class Cost(index: Int, text: String) extends AST:
    def toNodes =
      val outerNode = new StackPane with StyleClasses(GATCGUICSS.costTextBackground):
        children = new Text(text) with StyleClasses(GATCGUICSS.costText) :: Nil
      outerNode :: Nil

  case class ErrorCorrection(index: Int, error: Seq[AST], correction: Seq[AST]) extends AST:
    def toNodes =
      (childrenToStyledNodes(error, GATCGUICSS.errorText) :+ Text(" ")) ++ childrenToStyledNodes(correction, GATCGUICSS.correctionText)

  case class Redaction(index: Int, children: Seq[AST]) extends AST:
    def toNodes =
      childrenToStyledNodes(children, GATCGUICSS.redactionText)

  case class Addition(index: Int, children: Seq[AST]) extends AST:
    def toNodes =
      childrenToStyledNodes(children, GATCGUICSS.additionText)
