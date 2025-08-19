package com.oopman.collectioneer.plugins.gatcg.gui

import fastparse.*
import NoWhitespace.*
import scalafx.scene.Node

object GATCGTemplateLangParser:
  private val controlCharacters = Set('*', '[', ']', '(', ')', '%')

  private def Life[$: P]        = P( Index ~ "[LIFE]" ).map(GATCGTemplateLangAST.Life(_))
  private def Power[$: P]       = P( Index ~ "[POWER]" ).map(GATCGTemplateLangAST.Power(_))
  private def Rest[$: P]        = P( Index ~ "[REST]" ).map(GATCGTemplateLangAST.Rest(_))
  private def BoldText[$: P]    = P( Index ~ "**" ~ CharsWhile(_ != '*').! ~ "**" ).map((index, text) => GATCGTemplateLangAST.BoldText(index, text))
  private def ItalicText[$: P]  = P( Index ~ "*"  ~ CharsWhile(_ != '*').! ~ "*" ).map((index, text) => GATCGTemplateLangAST.ItalicText(index, text))
  private def BubbleText[$: P]  = P( Index ~ "["  ~ CharsWhile(_ != ']').! ~ "]" ).map((index, text) => GATCGTemplateLangAST.BubbleText(index, text))
  private def Cost[$: P]    = P( Index ~ "("  ~ CharsWhile(_ != ')').! ~ ")" ).map((index, text) => GATCGTemplateLangAST.Cost(index, text))
  private def Addition[$: P] = P( Index ~ "%%" ~ CharsWhile(_ != '%').! ~ "%" ).map((index, text) => GATCGTemplateLangAST.Addition(index, text))
  private def ErrorCorrection[$: P] = P( Index ~ "%" ~ CharsWhile(_ != '%').! ~ "%" ~ CharsWhile(_ != '%').! ~ "%" ).map((index, error, correction) => GATCGTemplateLangAST.ErrorCorrection(index, error, correction))
  private def Redaction[$: P] = P ( Index ~ "%" ~ CharsWhile(_ != '%').! ~ "%" ).map((index, text) => GATCGTemplateLangAST.Redaction(index, text))
  private def RegularText[$: P] = P( Index ~ CharsWhile(!controlCharacters.contains(_)).! ).map((index, text) => GATCGTemplateLangAST.RegularText(index, text))

  private def EffectTextElement[$: P] = P(Life | Power | Rest | BoldText | ItalicText | BubbleText | Cost | Addition | ErrorCorrection | Redaction | RegularText )
  def EffectText[$: P] = P( Start ~ EffectTextElement.rep ~ End )

  def produceNodes(text: String): Seq[Node] =
    val result: Parsed[Seq[GATCGTemplateLangAST.AST]] = parse(text, EffectText(using _))
    result match {
      case Parsed.Success(a, b) => a.flatMap(_.toNodes)
      case _ => Nil // TODO: Produce an error message
    }
