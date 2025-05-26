package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.cli.*
import scopt.{OParser, OParserBuilder}
import io.circe.Json

trait CLIPlugin extends Plugin:
  def getDefaultSubConfig: CLISubConfig
  def getActions(builder: OParserBuilder[CLIConfig]): List[(Verb, Subject, CLIConfig => Json, List[OParser[?, CLIConfig]])]
