package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.PropertyCollectionQueries
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection
import scalikejdbc.*

import java.sql.Connection

object PropertyCollectionDAO extends traits.dao.raw.PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int] =
    PropertyCollectionQueries
      .insert
      .batch(traits.entity.raw.PropertyCollection.propertyCollectionSeqToBatchInsertSeqSeq(propertyCollections): _*)
      .apply()

  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int] =
    raw.PropertyCollectionQueries
      .upsert
      .batch(traits.entity.raw.PropertyCollection.propertyCollectionSeqToBatchUpsertSeqSeq(propertyCollections): _*)
      .apply()

