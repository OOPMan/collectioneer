package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc.{NoExtractor, SQL}

trait CollectionRelatedCollectionPropertyValueSetQueries:
  def insert: SQL[Nothing, NoExtractor]

  def upsert: SQL[Nothing, NoExtractor]
