package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw.ScalikePropertyCollectionDAO
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.{AutoSession, DBSession}

import java.util.UUID

object PropertyCollectionDAOImpl extends ScalikePropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Seq[Int] =
    postgresbackend.queries.raw.PropertyCollectionQueries
      .insert
      .batch(postgresbackend.entity.raw.PropertyCollection.propertyCollectionSeqToBatchUpsertSeq(propertyCollections)*)
      .apply()

  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Seq[Int] =
    postgresbackend.queries.raw.PropertyCollectionQueries
      .upsert
      .batch(postgresbackend.entity.raw.PropertyCollection.propertyCollectionSeqToBatchUpsertSeq(propertyCollections)*)
      .apply()

  def deletePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Seq[Int] = ???

  def getAllMatchingPropertyPKs(propertyPKs: Seq[UUID])(implicit session: DBSession = AutoSession): Seq[PropertyCollection] = ???

  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): Seq[PropertyCollection] =
    postgresbackend.queries.raw.PropertyCollectionQueries
      .getAllMatchingCollectionPKs
      .bind(session.connection.createArrayOf("varchar", collectionPKs.toArray))
      .map(postgresbackend.entity.raw.PropertyCollection.apply(postgresbackend.entity.raw.PropertyCollection.pc1.resultName))
      .list
      .apply()
  
