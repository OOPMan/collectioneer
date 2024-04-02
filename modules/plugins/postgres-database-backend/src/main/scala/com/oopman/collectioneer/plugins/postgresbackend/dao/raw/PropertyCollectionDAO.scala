package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.traits.dao.raw.PropertyCollectionDAO
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.DBSession

import java.util.UUID

object PropertyCollectionDAO extends PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Array[Int] =
    postgresbackend.queries.raw.PropertyCollectionQueries
      .insert
      .batch(postgresbackend.entity.raw.PropertyCollection.propertyCollectionSeqToBatchUpsertSeq(propertyCollections): _*)
      .apply()

  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Array[Int] =
    postgresbackend.queries.raw.PropertyCollectionQueries
      .upsert
      .batch(postgresbackend.entity.raw.PropertyCollection.propertyCollectionSeqToBatchUpsertSeq(propertyCollections): _*)
      .apply()

  def deletePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Array[Int] = ???

  def getAllMatchingPropertyPKs(propertyPKs: Seq[UUID])(implicit session: DBSession): List[PropertyCollection] = ???

  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): List[PropertyCollection] = ???
  
