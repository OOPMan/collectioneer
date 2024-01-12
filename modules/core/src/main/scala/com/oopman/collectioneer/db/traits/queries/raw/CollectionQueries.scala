package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc.{NoExtractor, SQL}

import java.util.UUID

trait CollectionQueries:
  val insert: SQL[Nothing, NoExtractor]

  val upsert: SQL[Nothing, NoExtractor]

  val all: SQL[Nothing, NoExtractor]

  def allMatchingPKs(collectionPKs: Seq[UUID]): SQL[Nothing, NoExtractor]
