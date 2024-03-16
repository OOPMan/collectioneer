package com.oopman.collectioneer.db.traits.queries.projected

import scalikejdbc.{NoExtractor, SQL}

import java.util.UUID

trait PropertyValueQueries:
  def propertyValuesByCollectionPKs(pvsUUIDs: Seq[UUID], deleted: Seq[Boolean] = List(false)): SQL[Nothing, NoExtractor]
