package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc.{NoExtractor, SQL}

trait PropertyPropertyValueSetQueries:
  def insert: SQL[Nothing, NoExtractor]

  def upsert: SQL[Nothing, NoExtractor]
