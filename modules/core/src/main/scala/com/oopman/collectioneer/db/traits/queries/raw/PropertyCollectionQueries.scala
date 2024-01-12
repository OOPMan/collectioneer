package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc.{NoExtractor, SQL}
trait PropertyCollectionQueries:
  val insert: SQL[Nothing, NoExtractor]

  val upsert: SQL[Nothing, NoExtractor]
