package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.plugins.CLIPlugin
import izumi.distage.plugins.PluginDef

class GATCGCLIPlugin extends CLIPlugin:
  override def getName: String = "Grand Archive TCG"

  override def getVersion: String = "master"

object GATCGCLIPlugin extends PluginDef:
  many[CLIPlugin].add(GATCGCLIPlugin())
