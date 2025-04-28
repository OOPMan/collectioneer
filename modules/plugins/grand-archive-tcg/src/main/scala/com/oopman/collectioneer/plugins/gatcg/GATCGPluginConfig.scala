package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.cli.Subconfig

import java.io.File

case class GATCGPluginConfig
(
  grandArchiveTCGJSON: Option[File] = None,
  grandArchiveTCGImages: Option[File] = None
) extends Subconfig
