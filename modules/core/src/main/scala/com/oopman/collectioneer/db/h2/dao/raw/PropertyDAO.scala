package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.{entity, h2, traits}
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.PropertyQueries
import com.oopman.collectioneer.db.traits.entity.raw.Property
import scalikejdbc.*

import java.sql.Connection

object PropertyDAO extends traits.dao.raw.PropertyDAO:

  def createProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int] =
    PropertyQueries
      .insert
      .batch(traits.entity.raw.Property.propertiesSeqToBatchInsertSeqSeq(properties): _*)
      .apply()

  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int] =
    raw.PropertyQueries
      .upsert
      .batch(traits.entity.raw.Property.propertiesSeqToBatchUpsertSeqSeq(properties): _*)
      .apply()

  def getAll()(implicit session: DBSession = AutoSession): List[h2.entity.raw.Property] =
    raw.PropertyQueries
      .all
      .map(h2.entity.raw.Property(h2.entity.raw.p1.resultName))
      .list
      .apply()

  