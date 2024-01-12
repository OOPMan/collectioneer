package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc._

trait PropertyValueQueries:
  val insert: SQL[Nothing, NoExtractor]

  val upsert: SQL[Nothing, NoExtractor]

  val deleteByPK: SQL[Nothing, NoExtractor]

  val deleteByPropertyValueSetPksAndPropertyPKs: SQL[Nothing, NoExtractor]
