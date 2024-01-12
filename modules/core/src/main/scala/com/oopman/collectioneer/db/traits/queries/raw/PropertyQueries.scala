package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc._

trait PropertyQueries:
  val insert: SQL[Nothing, NoExtractor]

  val upsert: SQL[Nothing, NoExtractor]

  val all: SQL[Nothing, NoExtractor]
