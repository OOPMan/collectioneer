package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Property
import scalikejdbc.{AutoSession, DBSession}
import com.oopman.collectioneer.db.{entity, scalikejdbc, traits}
import com.oopman.collectioneer.plugins.postgresbackend

import java.util.UUID

object PropertyDAOImpl extends scalikejdbc.traits.dao.raw.ScalikePropertyDAO:

  def createProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.PropertyQueries
      .insert
      .batch(postgresbackend.entity.raw.Property.propertiesSeqToBatchInsertSeq(properties): _*)
      .apply()

  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.PropertyQueries
      .upsert
      .batch(postgresbackend.entity.raw.Property.propertiesSeqToBatchInsertSeq(properties): _*)
      .apply()

  def getAll(implicit session: DBSession = AutoSession): List[entity.raw.Property] =
    postgresbackend.queries.raw.PropertyQueries
      .all
      .map(postgresbackend.entity.raw.Property(postgresbackend.entity.raw.Property.p1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(propertyPKs: Seq[UUID])(implicit session: DBSession): List[Property] =
    postgresbackend.queries.raw.PropertyQueries
      .allMatchingPKs(propertyPKs)
      .map(postgresbackend.entity.raw.Property(postgresbackend.entity.raw.Property.p1.resultName))
      .list
      .apply()
