package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc.{NoExtractor, SQL}

import java.util.UUID

trait CollectionQueries:
  def insert: SQL[Nothing, NoExtractor]

  def upsert: SQL[Nothing, NoExtractor]

  def all: SQL[Nothing, NoExtractor]

  def allMatchingPKs(collectionPKs: Seq[UUID]): SQL[Nothing, NoExtractor]
