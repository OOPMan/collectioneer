package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.cli.{Config, Subject, Verb, Action}
import scopt.{OParserBuilder, OParser}

trait CLIPlugin {
  def getName: String
  def getShortName: String
  def getVersion: String

  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Action, List[OParser[_, Config]])]

}
