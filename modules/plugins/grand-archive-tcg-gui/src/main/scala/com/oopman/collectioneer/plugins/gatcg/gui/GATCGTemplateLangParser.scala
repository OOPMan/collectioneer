package com.oopman.collectioneer.plugins.gatcg.gui

import fastparse.*
import NoWhitespace.*
import scalafx.scene.Node

object GATCGTemplateLangParser:
  private val controlCharacters = Set('*', '[', ']', '%', '@')
  private def RegularText[$: P]: P[GATCGTemplateLangAST.RegularText] =
    P( Index ~ CharsWhile(!controlCharacters.contains(_)).! ).map((index, text) => GATCGTemplateLangAST.RegularText(index, text))

  private def Life[$: P]  = P( Index ~ "[LIFE]" ).map(GATCGTemplateLangAST.Life(_))
  private def Power[$: P] = P( Index ~ "[POWER]" ).map(GATCGTemplateLangAST.Power(_))
  private def Rest[$: P]  = P( Index ~ "[REST]" ).map(GATCGTemplateLangAST.Rest(_))

  private def BoldText[$: P]: P[GATCGTemplateLangAST.BoldText] =
    P( Index ~ "**" ~ BoldInnerText.rep(min = 1) ~ "**" )
      .map((index, innerASTs) => GATCGTemplateLangAST.BoldText(index, innerASTs))
  private def BoldInnerText[$: P]: P[GATCGTemplateLangAST.AST] =
    P( Life | Power | Rest | ItalicText | BubbleText | Cost | Addition | Redaction | ErrorCorrection | RegularText  )

  private def ItalicText[$: P]: P[GATCGTemplateLangAST.ItalicText] =
    P( Index ~ "*"  ~ ItalicInnerText.rep(min = 1) ~ "*" )
      .map((index, innerASTs) => GATCGTemplateLangAST.ItalicText(index, innerASTs))
  private def ItalicInnerText[$: P]: P[GATCGTemplateLangAST.AST] =
    P( Life | Power | Rest | BoldText | BubbleText | Cost | Addition | Redaction | ErrorCorrection | RegularText  )

  private def BubbleText[$: P]: P[GATCGTemplateLangAST.BubbleText] =
    P( Index ~ "["  ~ BubbleInnerText.rep(min = 1) ~ "]" )
      .map((index, innerASTs) => GATCGTemplateLangAST.BubbleText(index, innerASTs))
  private def BubbleInnerText[$: P]: P[GATCGTemplateLangAST.AST] =
    P( Life | Power | Rest | BoldText | ItalicText | Cost | Addition | Redaction | ErrorCorrection | RegularText  )

  private def Cost[$: P]: P[GATCGTemplateLangAST.Cost] =
    P( Index ~ "@"  ~ CostInnerText.rep(min = 1, max = 1).! ~ "@" )
      .map((index, text) => GATCGTemplateLangAST.Cost(index, text))
  private def CostLevel[$: P]     = P ( StringIn("LV", "Lv", "lv") )
  private def CostOperator[$: P]  = P ( StringIn(">", ">=", "=", "<", "<=", "+", "-") )
  private def CostValues[$: P]    = P ( CharIn("0-9").rep(min = 1) )
  private def CostVariable[$: P]  = P ( CharIn("A-Z").rep(min = 1, max = 1) )
  private def CostElement[$: P]   = P ( CostValues | CostLevel | CostVariable )
  private def CostInnerText[$: P] =
    P ( CostElement ~ (CostOperator ~ CostElement).? )

  private def Addition[$: P]: P[GATCGTemplateLangAST.Addition] =
    P( Index ~ "%%" ~ AdditionInnerText.rep(min = 1) ~ "%" )
      .map((index, innerASTs) => GATCGTemplateLangAST.Addition(index, innerASTs))
  private def ErrorCorrection[$: P]: P[GATCGTemplateLangAST.ErrorCorrection] =
    P( Index ~ "%"  ~ ErrorCorrectionInnerText.rep(min = 1) ~ "%" ~ ErrorCorrectionInnerText.rep(min = 1) ~ "%" )
      .map((index, errorASTs, correctionASTs) => GATCGTemplateLangAST.ErrorCorrection(index, errorASTs, correctionASTs))
  private def Redaction[$: P]: P[GATCGTemplateLangAST.Redaction] =
    P( Index ~ "%"  ~ RedactionInnerText.rep(min = 1) ~ "%%" )
      .map((index, innerASTs) => GATCGTemplateLangAST.Redaction(index, innerASTs))

  private def ErrorCorrectionInnerText[$: P]: P[GATCGTemplateLangAST.AST] =
    P( Life | Power | Rest | BoldText | ItalicText | BubbleText | Cost | RegularText )
  private def AdditionInnerText[$: P]: P[GATCGTemplateLangAST.AST] = ErrorCorrectionInnerText
  private def RedactionInnerText[$: P]: P[GATCGTemplateLangAST.AST] = AdditionInnerText

  private def StylizedText[$: P]: P[GATCGTemplateLangAST.AST] =
    P( Life | Power | Rest | BoldText | ItalicText | BubbleText | Cost | Addition | Redaction | ErrorCorrection )

  private def Text[$: P]: P[GATCGTemplateLangAST.AST] =
    P( RegularText | StylizedText )

  def EffectText[$: P]: P[Seq[GATCGTemplateLangAST.AST]] = P( Start ~ Text.rep ~ End )

  def produceNodes(text: String): Seq[Node] =
    val preparedText = text
      // Replace GATCG Cost specification with alternate form that is more distinct for parser handling. I got tired of trying to work out the right way to handle the bracket-ambiguity...
      .replaceAll("""\(((\d+|LV|[XYZ])(([+-]|<|>)(\d+|LV|[XYZ]))*)\) ?""", "@$1@")

    val result: Parsed[Seq[GATCGTemplateLangAST.AST]] = parse(preparedText, EffectText(using _))
    result match {
      case Parsed.Success(a, b) => a.flatMap(_.toNodes)
      case _ => Nil // TODO: Produce an error message
    }
