package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc.*

trait PropertyValueQueries:
  def insert: SQL[Nothing, NoExtractor]

  def upsert: SQL[Nothing, NoExtractor]

  def deleteByPK: SQL[Nothing, NoExtractor]

  def deleteByCollectionPksAndPropertyPKs: SQL[Nothing, NoExtractor]
