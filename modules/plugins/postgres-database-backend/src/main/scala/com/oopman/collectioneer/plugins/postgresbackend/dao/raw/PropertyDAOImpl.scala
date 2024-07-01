package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw.ScalikePropertyDAO
import com.oopman.collectioneer.db.traits.entity.raw.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.{AutoSession, DBSession}

import java.util.UUID

object PropertyDAOImpl extends ScalikePropertyDAO:

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

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession = AutoSession): List[Property] =
    val (comparisonSQL, parameters) = postgresbackend.PropertyValueQueryDSLSupport.comparisonsToSQL(comparisons)
    postgresbackend.queries.raw.PropertyQueries
      .innerJoiningPropertyCollection(s"($comparisonSQL)", "collection_pk", CollectionOfPropertiesOfProperty)
      .bind(parameters: _*)
      .map(resultSet => postgresbackend.entity.raw.Property.apply(resultSet))
      .list
      .apply()
