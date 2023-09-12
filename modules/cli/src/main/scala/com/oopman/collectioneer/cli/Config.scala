package com.oopman.collectioneer.cli

import java.io.File

case class Config
(
  // TODO: Make this an enum
  command: String = "initDB",
  collection: File = new File("./collection.db"),
  verbose: Boolean = false,
  debug: Boolean = false
)
