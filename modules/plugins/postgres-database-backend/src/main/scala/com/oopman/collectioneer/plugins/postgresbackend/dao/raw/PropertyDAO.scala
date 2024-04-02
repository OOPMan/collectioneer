package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Property
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID

object PropertyDAO extends traits.dao.raw.PropertyDAO:

  def createProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.PropertyQueries
      .insert
      .batch(traits.entity.raw.Property.propertiesSeqToBatchInsertSeq(properties): _*)
      .apply()

  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.PropertyQueries
      .upsert
      .batch(traits.entity.raw.Property.propertiesSeqToBatchInsertSeq(properties): _*)
      .apply()

  def getAll(implicit session: DBSession = AutoSession): List[entity.raw.Property] =
    postgresbackend.queries.raw.PropertyQueries
      .all
      .map(postgresbackend.entity.raw.Property(postgresbackend.entity.raw.Property.p1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(uuids: Seq[UUID])(session: DBSession): List[Property] = ???
