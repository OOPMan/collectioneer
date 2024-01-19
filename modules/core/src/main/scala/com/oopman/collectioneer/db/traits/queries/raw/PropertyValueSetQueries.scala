package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc._

trait PropertyValueSetQueries:
  def insert: SQL[Nothing, NoExtractor]

  def upsert: SQL[Nothing, NoExtractor]
