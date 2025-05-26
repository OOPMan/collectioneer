package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.SubConfig
import com.oopman.collectioneer.cli.CLISubConfig
import distage.ModuleDef

import java.io.File

case class GATCGPluginConfig
(
  grandArchiveTCGJSON: Option[File] = None,
  grandArchiveTCGImages: Option[File] = None
) extends CLISubConfig:
  def getModuleDefForSubConfig: ModuleDef = 
    val subConfig = this
    new ModuleDef:
      many[SubConfig].add(subConfig)
      many[CLISubConfig].add(subConfig)
      make[GATCGPluginConfig].from(subConfig)
  

