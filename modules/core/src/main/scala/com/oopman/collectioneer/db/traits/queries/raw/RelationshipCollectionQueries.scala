package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc.{NoExtractor, SQL}

trait RelationshipCollectionQueries:
  def insert: SQL[Nothing, NoExtractor]

  def upsert: SQL[Nothing, NoExtractor]
