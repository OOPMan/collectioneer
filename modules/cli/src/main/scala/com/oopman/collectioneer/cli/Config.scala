package com.oopman.collectioneer.cli

import java.io.File

enum Subjects:
  case collections extends Subjects
  case properties extends Subjects

enum Verbs(val subjects: List[Subjects] = Nil):
  case help extends Verbs
  case init extends Verbs
  case list extends Verbs(List(Subjects.collections, Subjects.properties))
  case get extends Verbs(List(Subjects.collections, Subjects.properties))

case class Config
(
  // TODO: Make this an enum
  command: (Verbs, Option[Subjects]) = (Verbs.help, None),
  collection: File = new File("./collection.db"),
  verbose: Boolean = false,
  debug: Boolean = false
)
