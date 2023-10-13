package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.cli.{Action, Config, Subconfig, Subject, Verb}
import scopt.{OParser, OParserBuilder}

trait CLIPlugin {
  def getName: String
  def getShortName: String
  def getVersion: String
  def getDefaultSubconfig: Subconfig
  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Action, List[OParser[_, Config]])]

}
