package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc.*

trait PropertyQueries:
  def insert: SQL[Nothing, NoExtractor]

  def upsert: SQL[Nothing, NoExtractor]

  def all: SQL[Nothing, NoExtractor]
