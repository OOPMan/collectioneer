package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.cli.*
import scopt.{OParser, OParserBuilder}
import io.circe.Json

trait CLIPlugin extends Plugin:
  def getDefaultSubconfig: Subconfig
  def getActions(builder: OParserBuilder[Config]): List[(Verb, Subject, Config => Json, List[OParser[_, Config]])]
