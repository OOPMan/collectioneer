package com.oopman.collectioneer.cli

import java.io.File

enum Subjects:
  case database extends Subjects
  case collections extends Subjects
  case properties extends Subjects

enum Verbs(val subjects: List[Subjects] = Nil):
  case help extends Verbs
  case create extends Verbs
  case delete extends Verbs
  case update extends Verbs
  case list extends Verbs(List(Subjects.collections, Subjects.properties))
  case get extends Verbs(List(Subjects.collections, Subjects.properties))

// TODO: Add sub-config classes for verbs

case class Config
(
  // TODO: Make this an enum
  verb: Verbs = Verbs.help,
  subject: Option[Subjects] = None,
  collection: File = new File("./collection.db"),
  verbose: Boolean = false,
  debug: Boolean = false
)
