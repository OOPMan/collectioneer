package com.oopman.collectioneer.db.traits.queries.raw

import scalikejdbc._

trait PropertyValueQueries:
  def insert: SQL[Nothing, NoExtractor]

  def upsert: SQL[Nothing, NoExtractor]

  def deleteByPK: SQL[Nothing, NoExtractor]

  def deleteByPropertyValueSetPksAndPropertyPKs: SQL[Nothing, NoExtractor]
