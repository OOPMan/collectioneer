package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.gui.StyleClasses
import scalafx.scene.Node
import scalafx.scene.layout.StackPane
import scalafx.scene.text.Text

object GATCGTemplateLangAST:
  sealed trait AST:
    def index: Int
    def toNodes: Seq[Node]

  case class RegularText(index: Int, text: String) extends AST:
    def toNodes = Text(text) :: Nil

  case class BoldText(index: Int, text: String) extends AST:
    def toNodes = new Text(text) with StyleClasses(GATCGUICSS.boldText) :: Nil

  case class ItalicText(index: Int, text: String) extends AST:
    def toNodes = new Text(text) with StyleClasses(GATCGUICSS.italicText) :: Nil

  case class BubbleText(index: Int, text: String) extends AST:
    def toNodes =
      val outerNode = new StackPane with StyleClasses(GATCGUICSS.bubbleTextBackground):
        children = new Text(text) with StyleClasses(GATCGUICSS.bubbleText) :: Nil
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

  case class ErrorCorrection(index: Int, error: String, correction: String) extends AST:
    def toNodes =
      new Text(error) with StyleClasses(GATCGUICSS.errorText) ::
      Text(" ") ::
      new Text(correction) with StyleClasses(GATCGUICSS.correctionText) ::
      Nil

  case class Redaction(index: Int, text: String) extends AST:
    def toNodes = new Text(text) with StyleClasses(GATCGUICSS.redactionText) :: Nil

  case class Addition(index: Int, text: String) extends AST:
    def toNodes = new Text(text) with StyleClasses(GATCGUICSS.additionText) :: Nil
