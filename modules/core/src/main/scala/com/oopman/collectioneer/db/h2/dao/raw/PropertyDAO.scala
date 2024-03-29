package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.PropertyQueries
import com.oopman.collectioneer.db.traits.entity.raw.Property
import com.oopman.collectioneer.db.{entity, h2, traits}
import scalikejdbc.*

import java.util.UUID

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

  def getAll(implicit session: DBSession = AutoSession): List[entity.raw.Property] =
    raw.PropertyQueries
      .all
      .map(entity.raw.Property(entity.raw.Property.p1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(uuids: Seq[UUID])(session: DBSession): List[Property] = ???
