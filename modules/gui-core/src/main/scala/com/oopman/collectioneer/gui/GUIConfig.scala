package com.oopman.collectioneer.gui

import com.oopman.collectioneer.Config

case class GUIConfig
(
  // TODO: Does this actually make sense? Database URI is defined at runtime in GUI mode...
  datasourceUri: String
) extends Config
