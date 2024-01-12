package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc._

trait PropertyValueSetQueries:
  val insert: SQL[Nothing, NoExtractor]

  val upsert: SQL[Nothing, NoExtractor]
